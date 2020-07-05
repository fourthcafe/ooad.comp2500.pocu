package dev.fourthcafe.pocutunes;

public class Program {
	public static void main(String[] args) {
		Song hotelCalifornia = new Song("Eagles", "Hotel California", 180100);
		Song heaven = new Song("Led Zeppelin", "Stairway to Heaven", 172100);
		Song houndDog = new Song("Elvis", "Hound Dog", 202800);
		Song santaBaby = new Song("Santa", "Baby", 292300);
		Song havana = new Song("Havana", "cafe", 292300);
		Song basketCase = new Song("Greenday", "Basket Case", 292300);
		Song christmas = new Song("Carry", "christmas", 292300);

		System.out.printf("%s by %s. Playtime is %d.\n", hotelCalifornia.getName(), hotelCalifornia.getArtist(),
				hotelCalifornia.getPlayTimeInMilliSeconds());


		final Playlist playlist1 = new Playlist("Classic Rock");
		playlist1.addSong(hotelCalifornia);
		playlist1.addSong(heaven);
		playlist1.addSong(houndDog);

		final Playlist playlist2 = new Playlist("Millenial");
		playlist2.addSong(havana);
		playlist2.addSong(santaBaby);

		final PocuTunes tunes = new PocuTunes();

		tunes.addSong(hotelCalifornia);
		tunes.addSong(heaven);
		tunes.addSong(houndDog);
		tunes.addSong(santaBaby);
		tunes.addSong(havana);
		tunes.addSong(basketCase);
		tunes.addSong(christmas);

		System.out.printf("Song count %d\n", tunes.getSoungCount());

		tunes.addPlaylist(playlist1);
		tunes.addPlaylist(playlist2);

		tunes.playSong("Basket Case");
		tunes.playSong("Hound Dog");

		// 없는 노래
		tunes.playSong("Escape");

		tunes.playPlaylist("Classic Rock");
		tunes.playPlaylist("Millenial");

		playlist2.setName("Christmas Music");
		playlist2.removeSong("Havana");
		playlist2.addSong(christmas);

		tunes.playPlaylist("Christmas Music");

		tunes.removeSong("Santa Baby");
		tunes.playPlaylist("Christmax Music");
		tunes.playSong("Santa Baby");

		tunes.removePlaylist("Christmas Music");

		System.out.printf("Song count %d.\n", tunes.getSoungCount());
		tunes.playPlaylist("Christmas Music");
	}
}
