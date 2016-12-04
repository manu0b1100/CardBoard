package com.example.manobhavjain.projectkasm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/29/2016.
 */
public class ProjectActivity extends AppCompatActivity implements PagerChanger{

    private ViewPager viewPager;

    private FloatingActionButton fab;
    private Project project;
    private TabLayout tab;


    public static Intent newInstanceEmpty(Context context, UUID uuid){
        Intent i=new Intent(context,ProjectActivity.class);
        i.putExtra("ProjectUUID",uuid);
        return i;

    }
    void updatedata(){
        UUID projectid=(UUID)getIntent().getSerializableExtra("ProjectUUID");
        project=ProjectsLab.get(this).getProject(projectid);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity);
        updatedata();
        viewPager=(ViewPager)findViewById(R.id.list_pager);
        viewPager.setOffscreenPageLimit(project.getListInsideProjects().size());

        tab=(TabLayout)findViewById(R.id.home_tab);

        fab=(FloatingActionButton)findViewById(R.id.floatingbutton1);


        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.i("mannu","the pos is "+position);
                return FragListInsideProject.newInstance(project.getUuid(),position,project.getListInsideProjects().get(position).getListofcards());
            }

            @Override
            public int getCount() {
                return project.getListInsideProjects().size();

            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return project.getListInsideProjects().get(position).getTitle();
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert=new AlertDialog.Builder(ProjectActivity.this);
                final EditText edittext = new EditText(ProjectActivity.this);
                alert.setMessage("Enter Your Message");
                alert.setTitle("Enter Your Title");

                alert.setView(edittext);

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        ListInsideProject listInsideProject=new ListInsideProject();
                        listInsideProject.setTitle(edittext.getText().toString());
                        project.getListInsideProjects().add(listInsideProject);

                        ProjectsLab.get(ProjectActivity.this).updateProject(project);
                        updatedata();
                        Log.i("mannu","size "+project.getListInsideProjects().size());

                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                });

                alert.show();

            }
        });

        tab.setupWithViewPager(viewPager);



    }
    @Override
    public void pagerleft() {
        viewPager.setCurrentItem((viewPager.getCurrentItem()-1)%project.getListInsideProjects().size());
    }

    @Override
    public void pagerright() {
        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%project.getListInsideProjects().size());


    }


}

