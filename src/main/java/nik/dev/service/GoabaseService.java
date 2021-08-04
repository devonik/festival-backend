package nik.dev.service;

import nik.dev.model.Festival;
import nik.dev.model.FestivalDetail;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.dto.goabase.GoabaseDetailDto;
import nik.dev.model.dto.goabase.GoabaseListDto;
import nik.dev.model.dto.goabase.GoabasePartyDetailDto;
import nik.dev.model.dto.goabase.GoabasePartyDto;
import nik.dev.repository.IFestivalDetailRepository;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GoabaseService {
    @Autowired
    IFestivalRepository festivalRepository;

    @Autowired
    IFestivalDetailRepository festivalDetailRepository;

    @Autowired
    IFestivalTicketPhaseRepository festivalTicketPhaseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void checkGoabaseParties() {
        ArrayList<GoabasePartyDto> goabaseParties = this.fetchGoabaseParties("DE", "festival", "festival");

        for (GoabasePartyDto goabaseParty : goabaseParties) {
            this.insertOrUpdateParty(this.fetchGoabaseParty(goabaseParty.getId()));
        }
    }

    private void insertOrUpdateParty(GoabaseDetailDto goabaseDetail) {
        GoabasePartyDetailDto goabasePartyDetail = goabaseDetail.getParty();
        //Thumbnail can not be null yet so ignore the entry
        if (goabasePartyDetail.getUrlImageFull() == null) return;

        Optional<Festival> festival = festivalRepository.findByGoabaseId(goabasePartyDetail.getId());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date startDate = formatter.parse(goabasePartyDetail.getDateStart());
            Date endDate = formatter.parse(goabasePartyDetail.getDateEnd());

            if (festival.isPresent()) {
                if (goabasePartyDetail.getNameStatus().equals("Cancelled")) {
                    festivalRepository.delete(festival.get());
                } else {
                    //Festival is present - need to update
                    FestivalDetail festivalDetail = null;
                    Optional<FestivalDetail> existingFestivalDetail = festivalDetailRepository.findByFestivalId(festival.get().getFestival_id());
                    if (existingFestivalDetail.isPresent()) {
                        festivalDetail = existingFestivalDetail.get();
                        if (festival.get().getFestival_detail_id() == null)
                            festival.get().setFestival_detail_id(existingFestivalDetail.get().getFestival_detail_id());
                    } else {
                        festivalDetail = new FestivalDetail();
                    }
                    festival.get().setGoabaseId(goabasePartyDetail.getId());
                    festival.get().setName(goabasePartyDetail.getNameParty());
                    festival.get().setDatum_start(startDate);
                    festival.get().setDatum_end(endDate);
                    festival.get().setThumbnail_image_url(goabasePartyDetail.getUrlImageFull());
                    Festival updatedFestival = festivalRepository.save(festival.get());

                    festivalDetail.setFestivalId(updatedFestival.getFestival_id());
                    festivalDetail.setGeoLatitude(Double.parseDouble(goabasePartyDetail.getGeoLat()));
                    festivalDetail.setGeoLongitude(Double.parseDouble(goabasePartyDetail.getGeoLon()));
                    try {
                        String description = goabasePartyDetail.getTextMore();
                        if(goabasePartyDetail.getNameOrganizer() != null) description += "\n\nWird organisiert von "+goabasePartyDetail.getNameOrganizer();
                        if(goabasePartyDetail.getTextOrganizer() != null) description += "\n"+goabasePartyDetail.getTextOrganizer();
                        if(goabasePartyDetail.getTextEntryFee() != null) description+= "\n\n\nEintritt\n\n"+goabasePartyDetail.getTextEntryFee();
                        festivalDetail.setDescription(StringEscapeUtils.escapeJava(description));
                    } catch (Exception ex) {
                        System.out.println("ERROR - Could not extract goabase description: " + ex.getMessage());
                    }

                    festivalDetail.setHomepage_url("<a href='" + goabasePartyDetail.getUrlParty() + "'>Homepage</a>");

                    festivalDetailRepository.save(festivalDetail);

                }

            } else if (!goabasePartyDetail.getNameStatus().equals("Cancelled")) {
                //Festival is new - need to insert
                Festival newFestival = new Festival();
                newFestival.setGoabaseId(goabasePartyDetail.getId());
                newFestival.setName(goabasePartyDetail.getNameParty());
                newFestival.setDatum_start(startDate);
                newFestival.setDatum_end(endDate);
                newFestival.setThumbnail_image_url(goabasePartyDetail.getUrlImageFull());
                Festival createdFestival = festivalRepository.save(newFestival);

                FestivalDetail newFestivalDetail = new FestivalDetail();

                newFestivalDetail.setFestivalId(createdFestival.getFestival_id());
                newFestivalDetail.setGeoLatitude(Double.parseDouble(goabasePartyDetail.getGeoLat()));
                newFestivalDetail.setGeoLongitude(Double.parseDouble(goabasePartyDetail.getGeoLon()));

                try {
                    String description = StringEscapeUtils.escapeJava(
                            goabasePartyDetail.getTextMore() +
                            "\n\nWird organisiert von " +
                            goabasePartyDetail.getNameOrganizer() +
                            "\n" +
                            goabasePartyDetail.getTextOrganizer());
                    newFestivalDetail.setDescription(description);
                } catch (Exception ex) {
                    System.out.println("ERROR - Could not extract goabase description: " + ex.getMessage());
                }
                newFestivalDetail.setHomepage_url("<a href='" + goabasePartyDetail.getUrlParty() + "'>Homepage</a>");

                FestivalDetail createdFestivalDetail = festivalDetailRepository.save(newFestivalDetail);

                if (createdFestival.getFestival_detail_id() == null) {

                    createdFestival.setFestival_detail_id(createdFestivalDetail.getFestival_detail_id());
                    festivalRepository.save(createdFestival);
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR - could not parse data from goabase. error:" + ex);
        }
    }


    private ArrayList<GoabasePartyDto> fetchGoabaseParties(String country, String eventtype, String search) {
        if (country == null) country = "DE";
        if (eventtype == null) eventtype = "festival";
        if (search == null) search = "festival";

        final String getGoabaseFestivalsURL = "https://goabase.net/api/party/json/?country=" + country + "&eventtype=" + eventtype + "&search=" + search;

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        GoabaseListDto goabaseList = restTemplate.getForObject(getGoabaseFestivalsURL, GoabaseListDto.class);

        assert goabaseList != null;
        return goabaseList.getPartylist();
    }

    private GoabaseDetailDto fetchGoabaseParty(Long id) {
        final String url = "https://goabase.net/api/party/json/" + id;

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        GoabaseDetailDto goabaseDetail = restTemplate.getForObject(url, GoabaseDetailDto.class);

        System.out.println("goabaseDetail: " + goabaseDetail.toString());

        return goabaseDetail;
    }
}
