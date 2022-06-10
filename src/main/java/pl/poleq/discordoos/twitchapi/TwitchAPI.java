//package pl.poleq.discordoos.twitchapi;
//
//import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
//import com.github.philippheuer.events4j.core.EventManager;
//import com.github.philippheuer.events4j.simple.SimpleEventHandler;
//import com.github.twitch4j.TwitchClient;
//import com.github.twitch4j.TwitchClientBuilder;
//
//public class TwitchAPI
//{
//    private TwitchClient client;
//    private EventManager eventManager;
//
//    public TwitchAPI()
//    {
//        client = TwitchClientBuilder.builder()
//                .withEnableHelix(true)
//                .withDefaultAuthToken(new OAuth2Credential("twitch","s6vqfa6w3v2llf09z9z4ephnbcpgj9"))
//                .withDefaultEventHandler(SimpleEventHandler.class)
//                .build();
//
////        client.getClientHelper().enableStreamEventListener("dawidtheblack");
//        eventManager = client.getEventManager();
//
//        TWLiveEvent myEventHandler = new TWLiveEvent();
//        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(myEventHandler);
//    }
//}
