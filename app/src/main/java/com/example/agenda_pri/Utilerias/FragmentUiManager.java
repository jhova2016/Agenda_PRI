package com.example.agenda_pri.Utilerias;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.agenda_pri.R;
import com.example.agenda_pri.UI.AgregarEvento;
import com.example.agenda_pri.UI.Principal;
import com.example.agenda_pri.UI.Propuestos;

public class FragmentUiManager{

    Fragment FragSchedulle;
    Fragment FragProposed;
    Fragment FragPromises ;
    FragmentTransaction fragmentTransaction;

    FragmentManager fragmentManager;
    public FragmentUiManager( FragmentManager fragmentManager,Fragment FragSchedulle, Fragment FragProposed, Fragment FragPromises ) {
    this.fragmentManager=fragmentManager;
        this.FragSchedulle=FragSchedulle;
        this.FragProposed=FragProposed;
        this.FragPromises=FragPromises;
    }

    public void FragmentUiManager(String Windows) {
        fragmentTransaction =fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) ;
        switch (Windows)
        {
            case "Schedulle":

                if(FragSchedulle.isAdded())
                {
                    fragmentTransaction.hide(FragPromises);
                    fragmentTransaction.hide(FragProposed);
                    fragmentTransaction.show(FragSchedulle);
                }
                else
                {
                    fragmentTransaction.hide(FragPromises);
                    fragmentTransaction.hide(FragProposed);
                    fragmentTransaction.add(R.id.main_fragment_placeholder,FragSchedulle,"0");
                    fragmentTransaction.show(FragSchedulle);

                }
                //fragmentTransaction.replace(R.id.main_fragment_placeholder, FragSchedulle);
                fragmentTransaction.commit();


                break;
            case "Proposed":

                if(FragProposed.isAdded())
                {
                    fragmentTransaction.hide(FragPromises);
                    fragmentTransaction.hide(FragSchedulle);
                    fragmentTransaction.show(FragProposed);
                }
                else
                {
                    fragmentTransaction.hide(FragPromises);
                    fragmentTransaction.hide(FragSchedulle);
                    fragmentTransaction.add(R.id.main_fragment_placeholder,FragProposed,"1");
                    fragmentTransaction.show(FragProposed);

                }
                //fragmentTransaction.replace(R.id.main_fragment_placeholder, FragSchedulle);
                fragmentTransaction.commit();


                break;

            case "Promised":

                if(FragPromises.isAdded())
                {
                    fragmentTransaction.hide(FragProposed);
                    fragmentTransaction.hide(FragSchedulle);
                    fragmentTransaction.show(FragPromises);
                }
                else
                {
                    fragmentTransaction.hide(FragProposed);
                    fragmentTransaction.hide(FragSchedulle);
                    fragmentTransaction.add(R.id.main_fragment_placeholder,FragPromises,"2");
                    fragmentTransaction.show(FragPromises);

                }
                //fragmentTransaction.replace(R.id.main_fragment_placeholder, FragSchedulle);
                fragmentTransaction.commit();


                break;
        }
    }



}
