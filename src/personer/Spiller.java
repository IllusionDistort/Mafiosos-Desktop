
package personer;

import personer.roller.Bestemor;
import personer.roller.Smith;

public class Spiller {

	String navn, gjenstand;
	Rolle rolle;
	Spiller offer;
	Rolle beskytter, forsvarer, redning, løgner, skjuler, kløne, drapsmann, smith, forsinkelse;
	int mafiarolle = 0;
	boolean lever = true, funker = true, død = false, beskyttet = false, forsvart = false, forsinket = false,
			reddet = false, skjult = false, løgn = false, kløna = false, klonet = false, fange = false, kidnappet = false,
			talt = false;

	public Spiller(String navn) {
		this.navn = navn;
		this.lever = true;
	}

	public Spiller(String navn, Rolle rolle) {
		this.navn = navn;
		this.rolle = rolle;
		this.lever = true;
	}

	public boolean pek(Spiller spiller){
		return rolle.pek(spiller);
	}

	public void drep(Rolle r){
		drapsmann = r;
		if(beskyttet){
			if(beskytter.id(Rolle.JESUS) && !id(Rolle.JESUS)){
				beskytter.spiller().snipe(r);
				beskyttet = false;
				beskytter = null;
			}
			return;
		}

		lever = false;
		rolle.drep();
	}

	public void snipe(Rolle r){
		lever = false;
		død = true;
		rolle.drep();
	}

	public void setLiv(boolean lever) {
		this.lever = lever;
	}
	
	public void setOffer(Spiller spiller) {
		offer = spiller;
	}

	public void tal(){
		talt = true;
	}
	
	public void henrett(){
		if(forsvart){
			if(forsvarer.id(Rolle.JESUS) && !id(Rolle.JESUS)){
				forsvarer.spiller().snipe(null);
				forsvart = false;
				forsvarer = null;
			} 
			return;
		}

		lever = false;
		stopp();
		rolle.drep();
	}

	public void sov(){
		talt = false;
		forsvart = false;
		forsvarer = null;
		beskyttet = false;
		beskytter = null;
		reddet = false;
		redning = null;
		skjult = false;
		skjuler = null;
		løgn = false;
		løgner = null;
		kløna = false;
		kløne = null;
		forsinket = false;
		forsinkelse = null;
		kidnappet = false;
		drapsmann = null;
		rolle.sov();
	}

	public void rens(Rolle r){
		if(beskytter == r){
			beskytter = null;
			beskyttet = false;
		}
		if(forsvarer == r){
			forsvarer = null;
			forsvart = false;
		}
		if(redning == r) {
			redning = null;
			reddet = false;
		}
		if(skjuler == r) {
			skjuler = null;
			skjult = false;
		}
		if(løgner == r) {
			løgner = null;
			løgn = false;
		}
		if(kløne == r) {
			kløne = null;
			kløna = false;
		}
		if(forsinkelse == r) {
			forsinkelse = null;
			forsinket = false;
		}
		if(drapsmann == r) {
			drapsmann = null;
		}
		if(r.id(Rolle.MAFIA) && !r.snill() && !død && !(r.id(Rolle.MAFIA) && id(Rolle.POLITI) && r.blokk == rolle)){
			vekk();
		}
		rolle.rens(r);
	}

	public void rensAlle(){
		beskytter = null;
		beskyttet = false;
		forsvarer = null;
		forsvart = false;
		redning = null;
		reddet = false;
		skjuler = null;
		skjult = false;
		løgner = null;
		løgn = false;
		kløne = null;
		kløna = false;
		forsinkelse = null;
		forsinket = false;
	}

	public void stopp(){
		lever = false;
		funker = false;
		klonet = false;
		rolle.funk(false);
	}

	public void vekk(){
		lever = true;
		funker = true;
		rolle.vekk();
	}

	public void beskytt(Rolle r){
		if(id(Rolle.BESTEMOR)) return;
		beskytter = r;
		beskyttet = true;
		if(!lever && !død) 
			vekk();
	}

	public void forsvar(Rolle r){
		if(id(Rolle.BESTEMOR)) return;
		forsvarer = r;
		forsvart = true;
	}

	public void redd(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		redning = r;
		reddet = true;
	}

	public void lyv(Rolle r){
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		løgn = true;
		løgner = r;
	}

	public void skjul(Rolle r){
		if(id(Rolle.BESTEMOR)  || id(Rolle.ILLUSJONIST)) return;
		skjuler = r;
		skjult = true;
	}

	public void kløn(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		kløne = r;
		kløna = true;
	}
	
	public void forsink(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		blokker(r);
		forsinkelse = r;
		rolle.forsinkelse = r;
		forsinket = true;
	}
	
	public void klonet(Rolle r) {
		smith = r;
		klonet = true;
	}

	public void klon() {
		if(id(Rolle.SMITH) || (beskyttet && !(id(Rolle.ILLUSJONIST) && rolle.offer.id(Rolle.SMITH)) || (this.rolle.id(Rolle.BESTEMOR) && ((Bestemor)this.rolle).flereBesøk())))
			return;
		rolle.drep();
		rolle.funk(false);
		((Smith)finnRolle(Rolle.SMITH)).klon(this);
		klonet = false;
	}
	
	public void kidnapp(Rolle r) {
		kidnappet = true;
		rolle.tv.spillere().kidnappSpiller(this);
	}
	
	public void fang() {
		fange = true;
	}
	
	public void befri() {
		fange = false;
	}

	public void blokker(Rolle blokk){
		rolle.blokker(blokk);
	}

	public void setRolle(Rolle rolle){
		this.rolle = rolle;
		if(rolle != null)
			rolle.setSpiller(this);
	}

	public void setGjenstand(String gjenstand){
		this.gjenstand = gjenstand;
	}

	//Get-metoder
	public String navn(){
		return navn;
	}

	public String tittel(){
		return rolle.tittel();
	}

	public boolean id(int i){
		return rolle.id(i);
	}

	public boolean lever(){
		return lever;
	}

	public boolean funker(){
		return funker;
	}
	
	public boolean talt(){
		return talt;
	}

	public boolean beskyttet(){
		return beskyttet;
	}

	public boolean forsvart(){
		if(forsvarer != null && forsvarer.id(Rolle.JESUS) && !forsvarer.lever()) forsvart = false;
		return forsvart;
	}

	public boolean reddet() {
		return reddet;
	}

	public boolean død() {
		return død;
	}
	
	public boolean løgn() {
		return løgn;
	}

	public boolean skjult() {
		return skjult;
	}

	public boolean kløna() {
		return kløna;
	}

	public boolean klonet() {
		return klonet;
	}

	public boolean fange() {
		return fange;
	}

	public boolean kidnappet() {
		return kidnappet;
	}
	
	public boolean forsinket() {
		return forsinket;
	}
	
	public Spiller offer(){
		return offer;
	}

	public Rolle rolle(){
		return rolle;
	}

	public Rolle beskytter() {
		return beskytter;
	}

	public Rolle forsvarer() {
		return forsvarer;
	}

	public Rolle redning() {
		return redning;
	}

	public Rolle skjuler() {
		return skjuler;
	}

	public Rolle løgner() {
		return løgner;
	}

	public Rolle kløne() {
		return kløne;
	}
	
	public Rolle forsinkelse() {
		return forsinkelse;
	}
	
	public Rolle drapsmann() {
		return drapsmann;
	}
	
	public Rolle smith() {
		return smith;
	}
	
	public int side(){
		return rolle.side;
	}

	public String gjenstand() {
		return gjenstand;
	}

	public void setMafiarolle(int rolle) {
		mafiarolle = rolle;
	}

	public int getMafiarolle() {
		return mafiarolle;
	}


	public Rolle finnRolle(int id) {
		return rolle.finnRolle(id);
	}

	public String toString(){
		return navn;
	}

	public String rapport(){
		String ut = navn;
		if(offer() != null) ut += " har valgt " + offer();
		else ut += " har ikke valgt noen";
		if(rolle.blokkert) ut += ", men ble blokkert av " + rolle.blokk;
		return ut;
	}

}
