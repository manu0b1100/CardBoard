package com.example.manobhavjain.projectkasm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {
    //private ViewPager viewPager;
    private TabLayout tabLayout;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;



    public void replaceFragment(Fragment newFragment){
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null)
        {
            fm.beginTransaction().add(R.id.fragment_container,newFragment).commit();
        }
        else
        {
            fm.beginTransaction().replace(R.id.fragment_container,newFragment).commit();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        Log.d("manobhav",prefs.getString(Config.EMAIL_SHARED_PREF,"def"));

        fragmentManager=getSupportFragmentManager();
        tabLayout=(TabLayout)findViewById(R.id.home_tab);
        tabLayout.addTab(tabLayout.newTab().setText("Cards"));
        tabLayout.addTab(tabLayout.newTab().setText("Projects"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        initMenuFragment();
        //initToolbar();
        replaceFragment(new Frag_Cards_List());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new Frag_Cards_List());
                        break;
                    case 1:
                        replaceFragment(new FragProjList());
                        break;
                    default:
                        replaceFragment(new Frag_Cards_List());
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Add Card");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Add Project");
        like.setResource(R.drawable.icn_2);

        MenuObject sync = new MenuObject("Sync");
        like.setResource(R.drawable.icn_2);

        MenuObject addFr = new MenuObject("Logout");
        like.setResource(R.drawable.icn_3);



        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(sync);
        menuObjects.add(addFr);

        return menuObjects;
    }
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = prefs.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addCard(){
        Cardbase cardbase=new Cardbase();



        CardsLab.get(this).addnote(cardbase);
        Intent i=card_activityEdit.newInstanceEmpty(this,cardbase.getId());
        startActivity(i);
    }
    private void addProject(){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Enter Your Title");

        alert.setView(edittext);

        alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Project project=new Project();
                project.setTitle(edittext.getText().toString());
                ProjectsLab.get(MainActivity.this).addProject(project);
                Intent i=ProjectActivity.newInstanceEmpty(MainActivity.this,project.getUuid());
                startActivity(i);
            }
        });

        alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void sync(){
        SyncData syncData=new SyncData(MainActivity.this);
        syncData.startUpdatingCards();
    }
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position){
            case 0:
                Toast.makeText(this, "0 clicked", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "1 clicked", Toast.LENGTH_SHORT).show();
                addCard();
                break;
            case 2:
                Toast.makeText(this, "2 clicked", Toast.LENGTH_SHORT).show();
                addProject();
                //sync();
                break;
            case 3:
                Toast.makeText(this, "2 clicked", Toast.LENGTH_SHORT).show();

                sync();
                break;
            case 4:
                Toast.makeText(this, "3 clicked", Toast.LENGTH_SHORT).show();
                logout();
                break;

        }

    }
}
