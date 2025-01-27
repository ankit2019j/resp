package ankit.com.vote_app_chandni_chowk;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class google_web extends AppCompatActivity {

    private ListView mListView;
    private WebView mapView;
    private ArrayList<String> myAddressList = new ArrayList<String>();
    private String myAddress;
    private String centerURL;;

    static final String[] COUNTRIES = new String[] {
            "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
            "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
            "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
            "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
            "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
            "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
            "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
            "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
            "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
            "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
            "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
            "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
            "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
            "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
            "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
            "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
            "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
            "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
            "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
            "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
            "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
            "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
            "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
            "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
            "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
            "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
            "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
            "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
            "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
            "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
            "Ukraine", "United Arab Emirates", "United Kingdom",
            "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
            "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
            "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_web);
        mListView = (ListView) findViewById(R.id.listView1);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, COUNTRIES));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // When clicked, show a toast with the TextView text
                Log.v("GoogleMapsWebViewActivity:", "Country: " + ((TextView) view).getText().toString());
                myAddressList.clear();
                String thisAddress = ((TextView) view).getText().toString();
                myAddressList.add(thisAddress);
                myAddress = thisAddress;
                //Set Map
                mapCustomerAddress();
            }
        });

        Button submit = (Button) findViewById(R.id.showMap);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri uri = Uri.parse("geo:0,0?q=" + myAddress);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }

    private void mapCustomerAddress(){

        boolean internet =  isNetworkAvailable(google_web.this);
        if(internet){
            Button mapButton = (Button) findViewById(R.id.showMap);
            mapButton.setEnabled(true);

            //getLocation();
            Log.v("GoogleMapsWebViewActivity:", "Address Size: " + myAddressList.size());
            Geocoder geoCoder = new Geocoder(google_web.this, Locale.getDefault());
            List<Address> addresses= null;
            try {
                for(int i=0;i<myAddressList.size();i++){
                    addresses = geoCoder.getFromLocationName(myAddressList.get(i), 5);
                    if(addresses.size() >0){
                        myAddress = myAddressList.get(i);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            setupWebView(addresses);
        }
        else {
            Button mapButton = (Button) findViewById(R.id.showMap);
            mapButton.setEnabled(false);

            mapView = (WebView) findViewById(R.id.mapView);
            String mapNotAvailable =     "<html><head></head><body>" +
                    "<img src='map_not_available.png' width='100%'/>" +
                    "<b>You need internet connectivity for Maps!</b>" +
            "</body><html>";
            mapView.loadDataWithBaseURL("file:///android_asset/", mapNotAvailable, "text/html", "utf-8", null);

        }

    }

    /** Sets up the WebView object and loads the URL of the page **/
    private void setupWebView(List<Address> addresses){
        Log.v("GoogleMapsWebViewActivity:", "Address Size: " + addresses.size());

        if (addresses.size() > 0) {
            Log.v("GoogleMapsWebViewActivity:", "Address Lat: " + addresses.get(0).getLatitude());
            Log.v("GoogleMapsWebViewActivity:", "Address Long: " + addresses.get(0).getLongitude());

            centerURL = "javascript:centerAt(" +
                    addresses.get(0).getLatitude() + "," +
                    addresses.get(0).getLongitude()+ ")";
        }

        mapView = (WebView) findViewById(R.id.mapView);
        mapView.getSettings().setJavaScriptEnabled(true);
        //Wait for the page to load then send the location information
        mapView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url)
            {
                mapView.loadUrl(centerURL);
            }

        });
        String MAP_URL = "http://10.0.2.2:8080/CapitalWebServices/androidMap.html";
        mapView.loadUrl(MAP_URL);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.v("INTERNET:",String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.v("INTERNET:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
