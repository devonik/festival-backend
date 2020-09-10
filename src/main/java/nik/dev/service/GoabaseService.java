package nik.dev.service;

import nik.dev.model.Festival;
import nik.dev.model.FestivalDetail;
import nik.dev.model.dto.goabase.GoabaseListDto;
import nik.dev.model.dto.goabase.GoabasePartyDto;
import nik.dev.repository.IFestivalDetailRepository;
import nik.dev.repository.IFestivalRepository;
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

    @PersistenceContext
    private EntityManager entityManager;

    public void checkGoabaseParties(){
        ArrayList<GoabasePartyDto> goabaseParties = this.fetchGoabaseParties("DE", "festival", "festival");

        for(GoabasePartyDto goabaseParty : goabaseParties){
            this.insertOrUpdateParty(goabaseParty);
        }
    }

    private void insertOrUpdateParty(GoabasePartyDto goabaseParty){
            //Thumbnail can not be null yet so ignore the entry
            if(goabaseParty.getUrlImageMedium() == null) return;

            Optional<Festival> festival = festivalRepository.findByGoabaseId(goabaseParty.getId());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            try {
                Date startDate = formatter.parse(goabaseParty.getDateStart());
                Date endDate = formatter.parse(goabaseParty.getDateStart());

                if (festival.isPresent()) {
                    if (goabaseParty.getNameStatus().equals("Cancelled")) {
                        festivalRepository.delete(festival.get());
                    } else {
                        //Festival is present - need to update
                        FestivalDetail festivalDetail = null;
                        Optional<FestivalDetail> existingFestivalDetail = festivalDetailRepository.findByFestivalId(festival.get().getFestival_id());
                        if(existingFestivalDetail.isPresent()){
                            festivalDetail = existingFestivalDetail.get();
                            if(festival.get().getFestival_detail_id() == null ) festival.get().setFestival_detail_id(existingFestivalDetail.get().getFestival_detail_id());
                        }else{
                            festivalDetail = new FestivalDetail();
                        }
                        festival.get().setGoabaseId(goabaseParty.getId());
                        festival.get().setName(goabaseParty.getNameParty());
                        festival.get().setDatum_start(startDate);
                        festival.get().setDatum_end(endDate);
                        festival.get().setThumbnail_image_url(goabaseParty.getUrlImageMedium());
                        Festival updatedFestival = festivalRepository.save(festival.get());

                        String homepageUrl = null;
                        if(goabaseParty.getUrlOrganizer() != null) homepageUrl = "<a href='"+goabaseParty.getUrlOrganizer()+"'>Homepage</a>";
                        if(goabaseParty.getUrlOrganizer() == null && goabaseParty.getUrlPartyHtml() != null) homepageUrl = "<a href='"+goabaseParty.getUrlPartyHtml()+"'>Homepage</a>";

                        festivalDetail.setFestivalId(updatedFestival.getFestival_id());
                        festivalDetail.setGeoLatitude(Double.parseDouble(goabaseParty.getGeoLat()));
                        festivalDetail.setGeoLongitude(Double.parseDouble(goabaseParty.getGeoLon()));
                        festivalDetail.setHomepage_url(homepageUrl);

                        festivalDetailRepository.save(festivalDetail);

                    }

                } else if (!goabaseParty.getNameStatus().equals("Cancelled")) {
                    //Festival is new - need to insert
                    Festival newFestival = new Festival();
                    newFestival.setGoabaseId(goabaseParty.getId());
                    newFestival.setName(goabaseParty.getNameParty());
                    newFestival.setDatum_start(startDate);
                    newFestival.setDatum_end(endDate);
                    newFestival.setThumbnail_image_url(goabaseParty.getUrlImageMedium());
                    Festival createdFestival = festivalRepository.save(newFestival);

                    FestivalDetail newFestivalDetail = new FestivalDetail();
                    String homepageUrl = null;
                    if(goabaseParty.getUrlOrganizer() != null) homepageUrl = goabaseParty.getUrlOrganizer();
                    if(goabaseParty.getUrlOrganizer() == null && goabaseParty.getUrlPartyHtml() != null) homepageUrl = goabaseParty.getUrlPartyHtml();

                    newFestivalDetail.setFestivalId(createdFestival.getFestival_id());
                    newFestivalDetail.setGeoLatitude(Double.parseDouble(goabaseParty.getGeoLat()));
                    newFestivalDetail.setGeoLongitude(Double.parseDouble(goabaseParty.getGeoLon()));
                    newFestivalDetail.setHomepage_url(homepageUrl);

                    FestivalDetail festivalDetail = festivalDetailRepository.save(newFestivalDetail);

                    if(festivalDetail.getFestival_detail_id() == null){

                        createdFestival.setFestival_detail_id(festivalDetail.getFestival_detail_id());
                        festivalRepository.save(createdFestival);
                    }
                }
            } catch (Exception ex) {
                System.out.println("ERROR - could not parse start & end date from goabase api. error:" + ex);
            }
        }


    private ArrayList<GoabasePartyDto> fetchGoabaseParties(String country, String eventtype, String search) {
        if(country == null) country = "DE";
        if(eventtype == null) eventtype = "festival";
        if(search == null) search = "festival";

        final String getGoabaseFestivalsURL = "https://goabase.net/api/party/json/?country="+country+"&eventtype="+eventtype+"&search=" + search;

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        GoabaseListDto goabaseList = restTemplate.getForObject(getGoabaseFestivalsURL, GoabaseListDto.class);

        System.out.println(goabaseList.getPartylist().size());

        assert goabaseList != null;
        return goabaseList.getPartylist();
    }
}
