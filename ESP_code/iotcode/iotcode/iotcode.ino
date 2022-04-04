//------------------------Library-----------
#include <OneWire.h>
#include <DallasTemperature.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <SimpleTimer.h>
#include <LiquidCrystal_I2C.h>
#include <DHT.h>
#include <IOXhop_FirebaseESP32.h>
#include <ArduinoJson.h>
#include <WiFi.h>

//--------------------------Firebase info------
#define FIREBASE_HOST "smarthydroponicfarm-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH
"ynVHEcSZYzv8lXPLh1nbtHqPFC9bVhx38qjMmjKB"
//-------------------------- Wifi INFO--------
#define WIFI_SSID "L2GEGI-NETWORK"
#define WIFI_PASSWORD "l2gegi24022021"
LiquidCrystal_I2C lcd (0x27,20,4);
//DHT
#define  dhtpin 7
#define  refroi 5
#define  motor1 13
#define  motor2 11
#define  chauf 3
String BtnTauto="0";
String Btnfan ="0";
String Btnheater="0";
String Tmin="25";
String Tmax="30";
#define DHTTYPE DHT11
DHT dht(dhtpin,DHTTYPE);
char temperature[] = "Temp = 00.0 C";
char humidity[] = "Hum = 00.0 %" ;
//Ph
SimpleTimer timer;
float calibration_value = -30  ;
int phval = 0; 
unsigned long int avgval; 
int buffer_arr[10],temp;
float ph_act;
String Btnalkaline="0";
String Btnacide="0";
String BtnPauto="0"; 
String Pmin="6.6";
String Pmax="7";
//lum
int RELAIS=6;
int ldr=A2;
int level;
String BtnLauto="0";
String Btnlamp ="0";
String Lmin="170";
String Lmax="175";
// ec
namespace pin {
const byte tds_sensor = A3;
const byte one_wire_bus = 7; // Dallas Temperature Sensor
}

 
namespace device {
float aref = 4.3;
}
 
namespace sensor {
float ec = 0;
unsigned int tds = 0;
float waterTemp = 0;
float ecCalibration = -1.25;
}
 
OneWire oneWire(pin::one_wire_bus);
DallasTemperature dallasTemperature(&oneWire);
String BtnEauto="0";
String BtnEC ="0";
String Emin="0.5";
String Emax="0.55";
void setup() {
 //Serial Begin at 9600 Baud
 Serial.begin(115200);
 delay(1000);
 WiFi.begin (WIFI_SSID,
WIFI_PASSWORD);
 while (WiFi.status() !=
WL_CONNECTED) {
 delay(500);
 Serial.print(".");
 }
  Serial.begin(9600); // Dubugging on hardware Serial 0
  lcd.backlight();
lcd.print("Acer :");
lcd.clear();
  // ec
  pinMode(13, OUTPUT);
  
  dallasTemperature.begin();
  //lum
  pinMode(RELAIS,OUTPUT);
  //ph
  Wire.begin();
 Serial.begin(9600);
 
 pinMode(10,OUTPUT);
  pinMode(12,OUTPUT);
pinMode(4,OUTPUT);
//DHT
lcd.init();                      // initialize the lcd 
  // Print a message to the LCD.
  dht.begin ();
  pinMode(refroi, OUTPUT);
  pinMode(chauf, OUTPUT);
  pinMode(motor1, OUTPUT);
  pinMode(motor2, OUTPUT);
digitalWrite(10,LOW);
digitalWrite(12,LOW);
digitalWrite(8,LOW);
digitalWrite(6,LOW);
digitalWrite(5,LOW);
digitalWrite(13,LOW);
digitalWrite(11,LOW);
digitalWrite(3,LOW);
}
void loop() {
  // DHT
  delay(1000);
  int Hum = dht.readHumidity();
  int Temp= dht.readTemperature();
  string h= string(Hum);
  string t= string(Temp);
  if (!(isnan(h) || isnan(t))) {
Firebase.setString("temperature", t);
Firebase.setString("humidity", h);}
  if (isnan(Hum)||isnan(Temp))
  {
    lcd.clear ();
    lcd.print("Error");
    return;
  }
  BtnTauto= Firebase.getString("/Tauto");
  Btnfan= Firebase.getString("/fan");
  Btnheater= Firebase.getString("/heater");
  Tmin= Firebase.getString("/minT");
  Tmax= Firebase.getString("/maxT");
if(BtnTauto="1")
{  
if( Temp<Tmin){
  digitalWrite(chauf,HIGH);
  digitalWrite(refroi,HIGH);
  digitalWrite(motor1,LOW);
  digitalWrite(motor2,HIGH);
  }
if (Temp>Tmax){
     digitalWrite(refroi,LOW);
    digitalWrite(chauf,LOW);
    digitalWrite(motor1,HIGH);
    digitalWrite(motor2,LOW);
    
}
if (Temp>Tmin && Tmax<30){
    digitalWrite(refroi,HIGH);
    digitalWrite(motor1,LOW);
  digitalWrite(motor2,LOW);
  digitalWrite(chauf,LOW);
}   
}
else{
  if(Btnfan == "1"){
 Serial.println("fan active (ON)");
 digitalWrite(refroi,LOW);
 }
 else {
 Serial.println("fan desactive (Off");
 digitalWrite(refroi,HIGH);
 }
 if(Btnheater == "1"){
 Serial.println("heater active (ON)");
 digitalWrite(chauf,LOW);
 }
 else {
 Serial.println("heater desactive (Off");
 digitalWrite(chauf,HIGH);
 }}
 
}
//fin dht

// Ph + niv
timer.run(); // Initiates SimpleTimer
 for(int i=0;i<10;i++) 
 { 
 buffer_arr[i]=analogRead(A1);
 delay(30);
 }
 for(int i=0;i<9;i++)
 {
 for(int j=i+1;j<10;j++)
 {
 if(buffer_arr[i]>buffer_arr[j])
 {
 temp=buffer_arr[i];
 buffer_arr[i]=buffer_arr[j];
 buffer_arr[j]=temp;
 }
 }
 }
 avgval=0;
 for(int i=2;i<8;i++)
 avgval+=buffer_arr[i];
 float volt=(float)avgval*5.0/1024/6; 
  ph_act = 5.70 * volt + calibration_value;
  Serial.print("ph");
  Serial.println(abs(ph_act));
    string P= string(ph_act);
  if (!(isnan(P)) {
Firebase.setString("PH", P);
}
 
 delay(1000);
  BtnPauto= Firebase.getString("/Pauto");
  Btnalkaline= Firebase.getString("/alkaline");
  Btnacide= Firebase.getString("/acide");
  Pmin= Firebase.getString("/minT");
  Pmax= Firebase.getString("/maxT");
 if(BtnPauto="1"){
 if (abs(ph_act)<Pmin){
  digitalWrite(12,HIGH);
  digitalWrite(10,LOW);
  delay(100);
  digitalWrite(10,HIGH);
 }
 if (abs(ph_act)>Pmax){
  digitalWrite(10,HIGH);
  digitalWrite(12,LOW);
  delay(100);
  digitalWrite(12,HIGH);
 }}
 else {
   if(Btnalkaline == "1"){
 Serial.println("alkaline pump active (ON)");
 digitalWrite(refroi,LOW);
 }
 else {
 Serial.println("alkaline pump desactive (Off");
 digitalWrite(refroi,HIGH);
 }
 if(Btnacide == "1"){
 Serial.println("acide pump active (ON)");
 digitalWrite(chauf,LOW);
 }
 else {
 Serial.println("acide pump desactive (Off");
 digitalWrite(chauf,HIGH);
 }}
 
//lum
 level= analogRead(ldr);
 level= map(level,0,1023,0,255);
 string l= string(level);
  if (!(isnan(l)) {
Firebase.setString("light", l);
}
 BtnLauto= Firebase.getString("/Lauto");
  Btnlamp= Firebase.getString("/Lamp");
  Lmin= Firebase.getString("/minL");
  Lmax= Firebase.getString("/maxL");
 if(BtnLauto="1")
 {
 if (level>maxL){
  digitalWrite(RELAIS,HIGH);
 }
  if (level<minL){
  digitalWrite(RELAIS,LOW);
 }}
 else{
  if(Btnlamp="0")
   digitalWrite(RELAIS,HIGH);
 }
 else{
  digitalWrite(RELAIS,LOW);
 }
 
 Serial.print("lum :");
 Serial.println(level);
 // fin lum
 // EC
 readTdsQuick();
 
 string E= string(sensor::ec);
  if (!(isnan(E)) {
Firebase.setString("EC", E);
}
  delay(1000);
  BtnEauto= Firebase.getString("/Eauto");
  BtnEC= Firebase.getString("/NutrientPump");
  Emin= Firebase.getString("/minE");
  Emax= Firebase.getString("/maxE");
 if(BtnEauto="1")
 {
  if (  sensor::ec  > Emax)
  {
    digitalWrite(8,LOW);
  }
  if (  sensor::ec  < Emin){
  digitalWrite(8,HIGH);
  }}
  else {
     if(BtnEC="0"){
   digitalWrite(8,HIGH);
 }
 else{
  digitalWrite(8,LOW);
 }}
  
    
// lcd DHT
lcd.setCursor(1,0);
lcd.print("Temp : ");
lcd.print(Temp);
lcd.print("degC");
lcd.setCursor(0,1);
lcd.print("Hum");
lcd.print(Hum);
lcd.print("%");
delay(2000);
lcd.clear();
 lcd.print("TDS   EC  pH Val");
  lcd.setCursor(0,1); 
  lcd.print(sensor::tds); 
  lcd.setCursor(5,1); 
  lcd.print(sensor::ec, 2); 
  lcd.setCursor(11,1); 
 lcd.print(abs(ph_act));
  delay(2000);
  lcd.clear();
  
}
 
void readTdsQuick() {
  dallasTemperature.requestTemperatures();
  sensor::waterTemp = dallasTemperature.getTempCByIndex(0);
  float rawEc = analogRead(pin::tds_sensor) * device::aref / 1024.0; // read the analog value more stable by the median filtering algorithm, and convert to voltage value
  float temperatureCoefficient = 1.0 + 0.02 * (sensor::waterTemp - 25.0); // temperature compensation formula: fFinalResult(25^C) = fFinalResult(current)/(1.0+0.02*(fTP-25.0));
  sensor::ec = (rawEc / temperatureCoefficient) * sensor::ecCalibration; // temperature and calibration compensation
  sensor::tds = (133.42 * pow(sensor::ec, 3) - 255.86 * sensor::ec * sensor::ec + 857.39 * sensor::ec) * 0.5; //convert voltage value to tds value
  Serial.print(F("TDS:")); Serial.println(sensor::tds);
  Serial.print(F("EC:")); Serial.println(sensor::ec, 2);
  Serial.print(F("Temperature:")); Serial.println(sensor::waterTemp,2);


  
//

}
 
