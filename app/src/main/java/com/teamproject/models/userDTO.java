package com.teamproject.models;

public class userDTO {

	private String ID_uzytkownika;
	private String imie;
	private String nazwisko;
	private String login;
	private String email;
	private String plec;
	private String wiek;
	private String klub;
	private String status;
	private String obywatelstwo;
	private String nr_tel;
	private String ICE;
	
	public userDTO(){}


	public String getID_uzytkownika(){
		return ID_uzytkownika;
	}
	public void setID_uzytkownika(String i){
		this.ID_uzytkownika = i;
	}
	public String getImie(){
		return imie;
	}
	public void setImie(String i){
		this.imie = i;
	}
	public String getNazwisko(){
		return nazwisko;
	}
	public void setNazwisko(String i){
		this.nazwisko = i;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String i){
		this.email = i;
	}
	public String getPlec(){
		return plec;
	}
	public void setPlec(String plec2){
		this.plec = plec2;
	}
	public String getWiek(){
		return wiek;
	}
	public void setWiek(String wiek2){
		this.wiek = wiek2;
	}
	public String getKlub(){
		return klub;
	}
	public void setKlub(String i){
		this.klub = i;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String i){
		this.status = i;
	}
	public String getObywatelstwo() {
		return obywatelstwo;
	}
	public void setObywatelstwo(String obywatelstwo) {
		this.obywatelstwo = obywatelstwo;
	}
	public String getNr_tel() {
		return nr_tel;
	}
	public void setNr_tel(String nr_tel) {
		this.nr_tel = nr_tel;
	}
	public String getICE() {
		return ICE;
	}
	public void setICE(String iCE) {
		ICE = iCE;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
}
