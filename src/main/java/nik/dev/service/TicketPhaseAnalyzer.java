package nik.dev.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import nik.dev.controller.PushController;
import nik.dev.controller.WhatsNewController;
import nik.dev.helper.Constants;
import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.WhatsNew;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IWhatsNewRepository;

@Service
public class TicketPhaseAnalyzer {
	@Autowired
	IFestivalRepository festivalRepository;
	@Autowired
	IFestivalTicketPhaseRepository festivalTicketPhaseRepository;
	@Autowired
	PushController pushController;
	@Autowired
	IWhatsNewRepository whatsNewRepository;
	
	public void start() {
		Optional<Festival> circus = festivalRepository.findById(Constants.PSYCHEDELIC_CIRCUS);
		if(circus.isPresent()) {
			FestivalTicketPhase oldCircusTicketPhase = festivalTicketPhaseRepository.findByFestivalAndSoldAndStarted(circus.get(), "no", "yes");
			
			if(getPsychedelicCircusPrice() != null && !getPsychedelicCircusPrice().equals(oldCircusTicketPhase.getPrice())) {
				System.out.println("OLD CIRCUS PRICE: "+oldCircusTicketPhase.getPrice());
				System.out.println("NEW CIRCUS PRICE: "+getPsychedelicCircusPrice());
				
				//There is a new Price on the Ticket Site
				
				//First we will insert the new Ticket Phase
				FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
				newTicketPhase.setFestival(circus.get());
				newTicketPhase.setTitle("VVK");
				newTicketPhase.setPrice(getPsychedelicCircusPrice());
				newTicketPhase.setSold("no");
				newTicketPhase.setStarted("yes");
				festivalTicketPhaseRepository.save(newTicketPhase);
				
				//Now we will set the old TicketPhase to sold
				oldCircusTicketPhase.setSold("yes");
				festivalTicketPhaseRepository.save(oldCircusTicketPhase);
				
				//Notify Phones about new Ticket Phase
				notifyNewTicketPhase(circus.get(), newTicketPhase);
			}
		}
		
		Optional<Festival> psyExp = festivalRepository.findById(Constants.PSYCHEDELIC_EXPERIENCE);
		if(psyExp.isPresent()) {
			FestivalTicketPhase oldPsyExpTicketPhase = festivalTicketPhaseRepository.findByFestivalAndSoldAndStarted(psyExp.get(), "no", "yes");
			if(getPsychedelicExperiencePrice() != null && !getPsychedelicExperiencePrice().equals(oldPsyExpTicketPhase.getPrice())) {
				System.out.println("OLD PSY EXP PRICE: "+oldPsyExpTicketPhase.getPrice());
				System.out.println("NEW PSY EXP PRICE: "+getPsychedelicExperiencePrice());
				
				//There is a new Price on the Ticket Site
				
				//First we will insert the new Ticket Phase
				FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
				newTicketPhase.setFestival(psyExp.get());
				newTicketPhase.setTitle("VVK");
				newTicketPhase.setPrice(getPsychedelicExperiencePrice());
				newTicketPhase.setSold("no");
				newTicketPhase.setStarted("yes");
				festivalTicketPhaseRepository.save(newTicketPhase);
				
				
				//Now we will set the old TicketPhase to sold
				oldPsyExpTicketPhase.setSold("yes");
				festivalTicketPhaseRepository.save(oldPsyExpTicketPhase);
				
				//Notify Phones about new Ticket Phase
				notifyNewTicketPhase(psyExp.get(), newTicketPhase);
			}
			
		}
		
		
	}
	
	private Double getPsychedelicCircusPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.PSYCHEDELIC_CIRCUS_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("uk-panel uk-panel-box");
			for (Element item : panelItems) {
				String collapseTitle = item.select(":root > h3").text();
				if(collapseTitle.equals("Tickets")) {
					Element table = item.select("table").get(0);
					Elements rows = table.select("tr");
					for(Element row : rows) {
						Elements cols = row.select("td");
						
						Element amountTicketSelect = row.select("select").get(0);
						String style = amountTicketSelect.attr("style");
						if(!style.equals("display: none;")){
							for(Element col : cols) {
								String classString = col.attr("class");
								if(classString.equals("price uk-text-right uk-width-2-5 uk-width-small-2-6")) {
									//Here we are in the Column where the Price is
									String price = col.select("span").get(0).text();
									String priceExtracted = price.replaceAll("[€ EUR]", "");
									return Double.parseDouble(priceExtracted);
									
								}
							}
							
						}
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getPsychedelicExperiencePrice() {



		Document doc;
		try {
			doc = Jsoup.connect(Constants.PSYCHEDELIC_EXPERIENCE_TICKET_URL).get();
			Element detailPrice = doc.getElementById("detailsPrice");
			NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
			Number number;
			try {
				number = format.parse(detailPrice.text());
				return number.doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private void notifyNewTicketPhase(Festival festival, FestivalTicketPhase festivalTicketPhase) {
		try {
			pushController.send(festival.getName()+" naechste Ticketphase", 
					"Der neue Preis eines Tickets betraegt: "+festivalTicketPhase.getPrice()+" Euro",
					"newTicketPhase");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Add Whats new Entry
		WhatsNew whatsNew = new WhatsNew();
		whatsNew.setContent("Neuer Preis ("+festivalTicketPhase.getPrice()+" Euro) eines Tickets für: "+festival.getName());
		whatsNewRepository.save(whatsNew);
	}
}
