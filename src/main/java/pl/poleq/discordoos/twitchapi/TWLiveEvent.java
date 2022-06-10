//package pl.poleq.discordoos.twitchapi;
//
//import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
//import com.github.twitch4j.events.ChannelGoLiveEvent;
//import com.github.twitch4j.helix.domain.Stream;
//
//public class TWLiveEvent
//{
//    @EventSubscriber
//    public void sendMsg(ChannelGoLiveEvent event)
//    {
//        Stream s = event.getStream();
//
//        System.out.println("NOWY STREAM!");
//        System.out.println("ID streamu: "+s.getId());
//        System.out.println("Użytkownik:");
//        System.out.println("ID: "+s.getUserId());
//        System.out.println("Name: "+s.getUserName());
//        System.out.println("Login: "+s.getUserLogin());
//        System.out.println("GRA:");
//        System.out.println("Nazwa: "+s.getId());
//        System.out.println("Nazwa: "+s.getGameName());
//        System.out.println("Coś");
//        System.out.println("Język: "+s.getLanguage());
//        System.out.println("Miniaturka:"+s.getThumbnailUrl());
//    }
//}
