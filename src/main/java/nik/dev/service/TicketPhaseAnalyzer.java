package nik.dev.service;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nik.dev.helper.Constants;
import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;

@Service
public class TicketPhaseAnalyzer {
	@Autowired
	IFestivalRepository festivalRepository;
	@Autowired
	IFestivalTicketPhaseRepository festivalTicketPhaseRepository;
	
	public void start() {
		Optional<Festival> circus = festivalRepository.findById(Constants.PSYCHEDELIC_CIRCUS);
		if(circus.isPresent()) {
			FestivalTicketPhase oldCircusTicketPhase = festivalTicketPhaseRepository.findByFestivalAndSoldAndStarted(circus.get(), "no", "yes");
			
			if(getPsychedelicCircusPrice() != oldCircusTicketPhase.getPrice()) {
				System.out.println("OLD CIRCUS PRICE: "+oldCircusTicketPhase.getPrice());
				System.out.println("NEW CIRCUS PRICE: "+getPsychedelicCircusPrice());
				
				//There is a new Price on the Ticket Site
				
				//First we will insert the new Ticket Phase
				FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
				newTicketPhase.setFestival(circus.get());
				newTicketPhase.setPrice(getPsychedelicCircusPrice());
				newTicketPhase.setSold("no");
				newTicketPhase.setStarted("yes");
				festivalTicketPhaseRepository.save(newTicketPhase);
				
				//Now we will set the old TicketPhase to sold
				oldCircusTicketPhase.setSold("yes");
				festivalTicketPhaseRepository.save(oldCircusTicketPhase);
			}
		}
		
		
	}
	private Double getPsychedelicCircusPrice() {
		Document doc;
		try {
			doc = Jsoup.connect("https://bit.ly/psycircus2019").get();
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
}
