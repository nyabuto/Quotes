package softikoda.com.quotes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import Database.manageDB;
import Quote.GetServerData;
import android.support.v7.widget.SearchView;

public class AllQuotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentData.OnFragmentInteractionListener,submit_quote.OnFragmentInteractionListener {
GetServerData serverData;
    SearchView searchView;
    FragmentData fdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

  if(isInternetAvailable()) {
            serverData = new GetServerData(getApplicationContext());
            serverData.serverQuotes(getApplicationContext());
        }
        else{
            Toast.makeText(getApplicationContext(),"Please connect to the internet to fetch new Quotes",Toast.LENGTH_SHORT).show();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("Submit Quote.");
               FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                submit_quote squote = new submit_quote();
                fragmentTransaction.replace(R.id.fragmentContainer,squote);
                fragmentTransaction.commit();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        LOAD ALL QUOTES HERE

        Bundle bundle = new Bundle();
        bundle.putInt("genre_id",0);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        FragmentData fragmentData = new FragmentData();
        fragmentData.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentData);
        fragmentTransaction.commit();

//        END OF LOADING DEFAULT



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.all_quotes, menu);

        final MenuItem menuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentData.listAdapter.filterData(query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                FragmentData.listAdapter.filterData(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        if (id == R.id.nav_all) {
            bundle.putInt("genre_id",0);
            setTitle("Legend Quotes");
        }

        else if (id == R.id.nav_political) {
            bundle.putInt("genre_id",1);
            setTitle("Political Quotes");
        }
        else if (id == R.id.nav_inspirational) {
            bundle.putInt("genre_id",2);
            setTitle("Inspirational Quotes");
        }
        else if (id == R.id.nav_love) {
            bundle.putInt("genre_id",3);
            setTitle("Love Quotes");
        }
        else if (id == R.id.nav_spiritual) {
            bundle.putInt("genre_id",4);
            setTitle("Spiritual Quotes");
        }
        else if (id == R.id.nav_science) {
            bundle.putInt("genre_id",5);
            setTitle("Science Quotes");
        }
        else if (id == R.id.nav_success) {
            bundle.putInt("genre_id",6);
            setTitle("Success Quotes");
        }
        else if (id == R.id.nav_motivational) {
            bundle.putInt("genre_id",7);
            setTitle("Motivational Quotes");
        }
        else if (id == R.id.nav_leadership) {
            bundle.putInt("genre_id",8);
            setTitle("Leadership Quotes");
        }


        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        FragmentData fragmentData = new FragmentData();
        fragmentData.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentData);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }
}
