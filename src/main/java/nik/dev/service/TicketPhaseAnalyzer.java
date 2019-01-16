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
		
		Optional<Festival> antaris = festivalRepository.findById(Constants.ANTARIS);
		checkForNewTicketPhases(antaris, getAntarisPrice());
		
		Optional<Festival> voov = festivalRepository.findById(Constants.VOOV);
		checkForNewTicketPhases(voov, getVoovPrice());
		
		Optional<Festival> simsalaboom = festivalRepository.findById(Constants.SIMSALABOOM);
		checkForNewTicketPhases(simsalaboom, getSimsalaboomPrice());
		
		Optional<Festival> shining = festivalRepository.findById(Constants.SHINING);
		checkForNewTicketPhases(shining, getShiningPrice());
		
		Optional<Festival> bachbylten = festivalRepository.findById(Constants.BACHBLYTEN);
		checkForNewTicketPhases(bachbylten, getBachbyltenPrice());
		
		Optional<Festival> indian = festivalRepository.findById(Constants.INDIAN_SPIRIT);
		checkForNewTicketPhases(indian, getIndianSpiritPrice());
		
	}
	
	private void checkForNewTicketPhases(Optional<Festival> festival, Double newPrice) {
		if(festival.isPresent()) {
				FestivalTicketPhase oldTicketPhase = festivalTicketPhaseRepository.findByFestivalAndSoldAndStarted(festival.get(), "no", "yes");

				if(oldTicketPhase == null || (newPrice != null && !newPrice.equals(oldTicketPhase.getPrice()))) {
					System.out.println("NEW PRICE FOR FESTIVAL TICKET: "+festival.get().getName());
					if(oldTicketPhase == null) {
						System.out.println("THERE WAS NO OLD TICKET PRICE");
					}else {
						System.out.println("OLD FESTIVAL PRICE: "+oldTicketPhase.getPrice());
					}
					
					System.out.println("NEW FESTIVAL PRICE: "+newPrice);
					
					//There is a new Price on the Ticket Site
					
					//First we will insert the new Ticket Phase
					FestivalTicketPhase newTicketPhase = new FestivalTicketPhase();
					newTicketPhase.setFestival(festival.get());
					newTicketPhase.setTitle("VVK");
					newTicketPhase.setPrice(newPrice);
					newTicketPhase.setSold("no");
					newTicketPhase.setStarted("yes");
					festivalTicketPhaseRepository.save(newTicketPhase);
					
					
					//Now we will set the old TicketPhase to sold
					oldTicketPhase.setSold("yes");
					festivalTicketPhaseRepository.save(oldTicketPhase);
					
					//Notify Phones about new Ticket Phase
					notifyNewTicketPhase(festival.get(), newTicketPhase);
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
	
	private Double getHaiInDenMaiPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.HAI_IN_DEN_MAI_TICKET_URL).get();
			Elements priceItems = doc.getElementsByClass("price--content content--default");
			for(Element item:priceItems) {
				String price = item.select(":root > meta").attr("content");
				return Double.parseDouble(price);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getWaldfriedenWonderlandPrice() {
			
		Document doc;
		try {
			doc = Jsoup.connect(Constants.WALDFRIEDEN_WONDERLAND_TICKET_URL).get();
			Elements priceItems = doc.getElementsByClass("price--content content--default");
			for(Element item:priceItems) {
				String price = item.select(":root > meta").attr("content");
				return Double.parseDouble(price);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getForrestExplosionPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.FORREST_EXPLOSION_TICKET_URL).get();
			Elements priceElements = doc.getElementsByClass("h1 stage__promo");
			for(Element priceEl : priceElements) {
				String priceString = priceEl.text();
				String priceExtracted = priceString.replaceAll("[^\\d.]", "");
				return Double.parseDouble(priceExtracted);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getVoovPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.VOOV_TICKET_URL).get();
			Element priceElement = doc.getElementsByClass("price uk-text-right uk-width-2-5 uk-width-small-2-6").get(0);
			String priceString = priceElement.select("input").attr("data-total-price-incl");
			String priceExtracted = priceString.replaceAll("[€ EUR]", "");
			return Double.parseDouble(priceExtracted);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private Double getSimsalaboomPrice() {
		
		Document doc;
		try {
			doc = Jsoup.connect(Constants.SIMSALABOOM_TICKET_URL).get();
			Elements priceRows = doc.getElementsByClass("row-fluid product-row simple");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getShiningPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.SHINING_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("uk-panel uk-panel-box");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getBachbyltenPrice() {
		Document doc;
		try {
			doc = Jsoup.connect(Constants.BACHBLYTEN_TICKET_URL).get();
			Elements panelItems = doc.getElementsByClass("uk-panel uk-panel-box");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private Double getIndianSpiritPrice() {


		Document doc;
		try {
			doc = Jsoup.connect(Constants.INDIAN_SPIRIT_TICKET_URL).get();
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
