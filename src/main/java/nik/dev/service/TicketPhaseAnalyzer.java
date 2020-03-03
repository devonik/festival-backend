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
		checkForNewTicketPhases(circus, getPsychedelicCircusPrice());
		
		Optional<Festival> psyExp = festivalRepository.findById(Constants.PSYCHEDELIC_EXPERIENCE);
		checkForNewTicketPhases(psyExp, getPsychedelicExperiencePrice());
		
		Optional<Festival> haiInDenMai = festivalRepository.findById(Constants.HAI_IN_DEN_MAI);
		checkForNewTicketPhases(haiInDenMai, getHaiInDenMaiPrice());
		
		Optional<Festival> waldfriedenWonderland = festivalRepository.findById(Constants.WALDFRIEDEN_WONDERLAND);
		checkForNewTicketPhases(waldfriedenWonderland, getWaldfriedenWonderlandPrice());
	
		Optional<Festival> forrestExplosion = festivalRepository.findById(Constants.FORREST_EXPLOSION);
		checkForNewTicketPhases(forrestExplosion, getForrestExplosionPrice());
		
		//Optional<Festival> antaris = festivalRepository.findById(Constants.ANTARIS);
		//checkForNewTicketPhases(antaris, getAntarisPrice());
		
		Optional<Festival> voov = festivalRepository.findById(Constants.VOOV);
		checkForNewTicketPhases(voov, getVoovPrice());
		
		Optional<Festival> simsalaboom = festivalRepository.findById(Constants.SIMSALABOOM);
		checkForNewTicketPhases(simsalaboom, getSimsalaboomPrice());
		
		//Optional<Festival> shining = festivalRepository.findById(Constants.SHINING);
		//checkForNewTicketPhases(shining, getShiningPrice());
		
		Optional<Festival> bachbylten = festivalRepository.findById(Constants.BACHBLYTEN);
		checkForNewTicketPhases(bachbylten, getBachbyltenPrice());
		
		Optional<Festival> indian = festivalRepository.findById(Constants.INDIAN_SPIRIT);
		checkForNewTicketPhases(indian, getIndianSpiritPrice());
		
		//Ticket page is to broken - i wont do that
		//Optional<Festival> earthquake = festivalRepository.findById(Constants.EARTHQUAKE);
		//checkForNewTicketPhases(earthquake, getEarthquakePrice());
		
	}
	
	private void checkForNewTicketPhases(Optional<Festival> festival, Double newPrice) {
		if(festival.isPresent()) {
			System.out.println("check new ticket phase for festival: ["+festival.get().getName()+"]");
				Optional<FestivalTicketPhase> oldTicketPhaseOptional = festivalTicketPhaseRepository.findByFestivalAndSoldAndStarted(festival.get(), "no", "yes");

					
				if(oldTicketPhaseOptional.isPresent()){
					FestivalTicketPhase oldTicketPhase = oldTicketPhaseOptional.get();
					System.out.println("Old Ticket Phase found. New Price: ["+newPrice+"]");
					if(newPrice != null) {
						if (!newPrice.equals(oldTicketPhase.getPrice())) {
							System.out.println("NEW PRICE FOR FESTIVAL TICKET: " + festival.get().getName());
							System.out.println("NEW FESTIVAL PRICE: " + newPrice);
							//If there is no old TicketPhase OR the newPrice is different then the old one
							//Save the new price

							//There is a new Price on the Ticket Site

							//First we will insert the new Ticket Phase
							FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
							newTicketPhase.setFestival(festival.get());
							newTicketPhase.setTitle("VVK");
							newTicketPhase.setPrice(newPrice);
							newTicketPhase.setSold("no");
							newTicketPhase.setStarted("yes");
							festivalTicketPhaseRepository.save(newTicketPhase);

							//Notify Phones about new Ticket Phase
							notifyNewTicketPhase(festival.get(), newTicketPhase);

							//If oldTicketPhase exist and new price is not equal
							//Then we set old as sold
							oldTicketPhase.setSold("yes");
							festivalTicketPhaseRepository.save(oldTicketPhase);
						}
					}else{
						//Ticketsphases are over - Festival is done
						//So we set old
						System.out.println("New Price is null so the festival is done");
						oldTicketPhase.setSold("yes");
						festivalTicketPhaseRepository.save(oldTicketPhase);
					}


				}else if(newPrice != null){
					System.out.println("No old Ticket Phase found. The newPrice ["+newPrice+"] will be saved");
					FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
					newTicketPhase.setFestival(festival.get());
					newTicketPhase.setTitle("VVK");
					newTicketPhase.setPrice(newPrice);
					newTicketPhase.setSold("no");
					newTicketPhase.setStarted("yes");
					festivalTicketPhaseRepository.save(newTicketPhase);

					//Notify Phones about new Ticket Phase
					notifyNewTicketPhase(festival.get(), newTicketPhase);
				}
		}
	}
	
	private Double getPsychedelicCircusPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.PSYCHEDELIC_CIRCUS_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("products products--row");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(panelItems == null) {
				return null;
			}
			for (Element item : panelItems) {
				if(!item.hasClass("products__item--sold-out")){
					//If the current item doesn't have the class sold out its the current price
					Element currentTicket = item.getElementsByClass("amount transition-color").get(0);
					if(currentTicket != null){
						String priceExtracted = currentTicket.text().replaceAll("[€&nbsp;]", "");
						return Double.parseDouble(priceExtracted);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for psy circus:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getPsychedelicExperiencePrice() {

		Document doc;
		try {
			doc = Jsoup.connect(Constants.PSYCHEDELIC_EXPERIENCE_TICKET_URL).get();
			Element detailPrice = doc.getElementById("detailsPrice");
			System.out.println("detailPrice: "+detailPrice);
			
			//detailPrice is null when it doesnt exist anymore - maybe the festival is done
			if(detailPrice == null) {
				return null;
			}else {
				NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
				Number number;
				try {
					number = format.parse(detailPrice.text());
					return number.doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for psychedelic experience:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getHaiInDenMaiPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.HAI_IN_DEN_MAI_TICKET_URL).get();
			
			Elements priceItems = doc.getElementsByClass("price--content content--default");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(priceItems == null) {
				return null;
			}
			for(Element item:priceItems) {
				String price = item.select(":root > meta").attr("content");
				return Double.parseDouble(price);
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for Hai in den Mai:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getWaldfriedenWonderlandPrice() {
			
		Document doc;
		try {
			doc = Jsoup.connect(Constants.WALDFRIEDEN_WONDERLAND_TICKET_URL).get();
			Elements priceItems = doc.getElementsByClass("price--content content--default");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(priceItems == null) {
				return null;
			}
			for(Element item:priceItems) {
				String price = item.select(":root > meta").attr("content");
				return Double.parseDouble(price);
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for waldfrieden:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getForrestExplosionPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.FORREST_EXPLOSION_TICKET_URL).get();
			Element body = doc.getElementsByClass("stage__body").get(0);
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(body == null) {
				return null;
			}else{
				Element ticketButton = body.getElementsByTag("a").get(0);
				if(ticketButton != null){
					String priceString = ticketButton.attr("title");
					String priceExtracted = priceString.replaceAll("[^\\d.]", "");
					return Double.parseDouble(priceExtracted);
				}
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for forrest explosion:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getAntarisPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.ANTARIS_TICKET_URL).get();
			String priceString = doc.select(".sell_amount_aktiv").text();
			String priceExtracted = priceString.replaceAll("[ €]", "");
			NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
			Number number;
			try {
				number = format.parse(priceExtracted);
				return number.doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			System.out.println("TicketPhase for antaris:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getVoovPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.VOOV_TICKET_URL).get();
			Element priceElement = doc.getElementsByClass("ticketSinglePrice").get(0);
			if(priceElement != null){
				String priceString = priceElement.text();
				String dumbStr = priceString.replaceAll("[Cost: Eur]", "");
				String priceExtracted = dumbStr.replace(",", ".");
				return Double.parseDouble(priceExtracted);
			}else{
				return null;
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for voov:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
	}

	private Double getSimsalaboomPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.SIMSALABOOM_TICKET_URL).get();
			Elements priceRows = doc.getElementsByClass("row-fluid product-row simple");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(priceRows == null) {
				return null;
			}
			for(Element priceRow : priceRows) {
				Elements cols = priceRow.select("div");
				for(Element col : cols) {
					String classString = col.attr("class");
					if(classString.equals("col-md-2 col-xs-6 availability-box available")) {
						String priceString = priceRow.getElementsByClass("col-md-2 col-xs-6 price").get(0).text();
						String priceExtracted = priceString.replaceAll("[€ ]", "");
						NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
						Number number;
						try {
							number = format.parse(priceExtracted);
							return number.doubleValue();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		} catch (IOException e) {
			System.out.println("TicketPhase for simsalaboom:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getShiningPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.SHINING_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("uk-panel uk-panel-box");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(panelItems == null) {
				return null;
			}
			for (Element item : panelItems) {
					Element table = item.select("table").get(0);
					
					Elements rows = table.select("tr");
					for(Element row : rows) {
						Elements amountCol = row.getElementsByClass("amount uk-text-center uk-width-1-5 uk-width-small-1-6");
						if(amountCol.size() > 0) {
							Element amountTicketSelect = amountCol.select("select").get(0);
							String style = amountTicketSelect.attr("style");
							if(!style.equals("display: none;")) {
								Element priceCol = row.getElementsByClass("price uk-text-right uk-width-2-5 uk-width-small-2-6").get(0);
								//Here we are in the Column where the Price is
								String price = priceCol.select("input").attr("data-total-price-incl");
								
								String priceExtracted = price.replaceAll("[€ EUR]", "");
								return Double.parseDouble(priceExtracted);
							}
						}
						
					}
				
				
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for shining:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getBachbyltenPrice() {
		Document doc;
		try {
			doc = Jsoup.connect(Constants.BACHBLYTEN_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("uk-panel uk-panel-box");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(panelItems == null) {
				return null;
			}
			for (Element item : panelItems) {
					Element table = item.select("table").get(0);
					Elements rows = table.select("tr");
					for(Element row : rows) {
						Elements amountCol = row.getElementsByClass("amount uk-text-center uk-width-1-5 uk-width-small-1-6");
						
						if(amountCol.size() > 0) {
							Element amountTicketSelect = amountCol.select("select").get(0);
							String style = amountTicketSelect.attr("style");
							if(!style.equals("display: none;")) {
								Element priceCol = row.getElementsByClass("price uk-text-right uk-width-2-5 uk-width-small-2-6").get(0);
								//Here we are in the Column where the Price is
								String price = priceCol.select("input").attr("data-total-price-incl");
								
								String priceExtracted = price.replaceAll("[€ EUR]", "");
								return Double.parseDouble(priceExtracted);
							}
						}
						
					}
				
				
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for bachbylten:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	
	private Double getIndianSpiritPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.INDIAN_SPIRIT_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("products__col");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(panelItems == null) {
				return null;
			}
			for (Element block : panelItems) {
				Elements items = block.getElementsByClass("products__item");
				for(Element item: items) {
					if(!item.hasClass("products__item--sold-out")){
						//If the current item doesn't have the class sold out its the current price
						Element currentTicket = item.getElementsByClass("amount transition-color").get(0);
						if(currentTicket != null){
							String priceExtracted = currentTicket.text().replaceAll("[€&nbsp;]", "");
							return Double.parseDouble(priceExtracted);
						}
					}
				}
				
					
				
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for indian spirit:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}
	//Ticket page is to broken - i wont do that
	/*private Double getEarthquakePrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.EARTHQUAKE_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("container");
			//element is null when it doesnt exist anymore - maybe the festival is done
			if(panelItems == null) {
				return null;
			}
			Elements divs = panelItems.select("div");
			Elements divs2 = divs.get(1).select("div");
			System.out.println("panelItems"+panelItems);
			
				
					
				
			}
		} catch (IOException e) {
			System.out.println("TicketPhase for indian spirit:");
			System.out.println("The ticket page cant be found maybe the festival is over");
			return null;
		}
		return null;
	}*/
		
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
