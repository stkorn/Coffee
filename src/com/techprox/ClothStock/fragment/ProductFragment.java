package com.techprox.ClothStock.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techprox.ClothStock.*;
import com.techprox.ClothStock.R;
import com.techprox.ClothStock.adapter.ImageAdapter;
import com.techprox.ClothStock.adapter.ProductAdapter;
import com.techprox.ClothStock.model.ProductItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stkornsmc on 2/7/14 AD.
 */
public class ProductFragment extends Fragment {
    Button accountBtn;
    Button startOrder;
    Dialog myDialog;
    Dialog regisDialog;
    AlertDialog logoutDialog;
    AlertDialog regisAlert;
    AlertDialog failregisAlert;
    Context mContext;
    private SharedPreferences prefs;

    LinearLayout  loggedIn;
    TextView user;
    Button logout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        prefs = mContext.getSharedPreferences(
                "com.techprox.ClothStock", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ImageAdapter mAdapter = new ImageAdapter(getActivity());
        viewPager.setAdapter(mAdapter);

        loggedIn = (LinearLayout) view.findViewById(R.id.loggedin);
        user = (TextView) view.findViewById(R.id.showUser);
        logout = (Button) view.findViewById(R.id.logout);

        accountBtn = (Button) view.findViewById(R.id.accountbtn);
        startOrder = (Button) view.findViewById(R.id.startorder);


        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });

        startOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logged = prefs.getString("email", "");

                if (!logged.matches("")) {
                    Intent i = new Intent(getActivity(), MenuOrder.class);
                    startActivity(i);
                } else {
                    AlertDialog accessAlert = new AlertDialog.Builder(getActivity())
                            .setTitle("Login first")
                            .setMessage("You must log in to order drinks")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    myDialog.show();

                                }
                            }).show();
                }

            }
        });

        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.loginview);
        myDialog.setCancelable(true);
        myDialog.setTitle("๊User Login");

        regisDialog = new Dialog(getActivity());
        regisDialog.setContentView(R.layout.register);
        regisDialog.setCancelable(true);
        regisDialog.setTitle("Register new account");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Log out")
                        .setMessage("Are you sure to logged out from this account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                changeStatus();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   logoutDialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();

            }
        });

        changeStatus();


        return view;
    }

    private void changeStatus() {
        String name = prefs.getString("name", "");
        if (!name.matches("")) {
             loggedIn.setVisibility(View.VISIBLE);
            accountBtn.setVisibility(View.GONE);
            accountBtn.setEnabled(false);
            user.setText(name);
        }  else {
            accountBtn.setVisibility(View.VISIBLE);
            accountBtn.setEnabled(true);
            loggedIn.setVisibility(View.GONE);
        }
    }

    private void callLoginDialog()
    {

        final Button login = (Button) myDialog.findViewById(R.id.login);
        Button newAc = (Button) myDialog.findViewById(R.id.createac);

        Button close = (Button) regisDialog.findViewById(R.id.cancelRegis);
        final Button regisAccount = (Button) regisDialog.findViewById(R.id.regis);

        final EditText emailLog = (EditText) myDialog.findViewById(R.id.email);
        final EditText passLog = (EditText) myDialog.findViewById(R.id.password);

        final EditText name = (EditText) regisDialog.findViewById(R.id.name);
        final EditText email = (EditText) regisDialog.findViewById(R.id.regisemail);
        final EditText password = (EditText) regisDialog.findViewById(R.id.pass);
        final EditText repassword = (EditText) regisDialog.findViewById(R.id.repass);

        //Register layout



        regisAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regisAccount.setEnabled(false);
//                Toast.makeText(mContext, "Click", 500).show();
                String nameRegis = name.getText().toString();
                String emailRegis = email.getText().toString();
                String passwordRegis = password.getText().toString();
                String rePassword = repassword.getText().toString();


                if (passwordRegis.matches(rePassword)) {
                    if (passwordRegis.length() > 3) {
                        String uri = String.format("http://10.0.2.2/coffee/public/register?name=%1$s&email=%2$s&pass=%3$s",
                                nameRegis, emailRegis, passwordRegis);

                        JsonObjectRequest regisReq = new JsonObjectRequest(Request.Method.GET,
                                uri,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response.has("success")) {
                                            try {
                                                boolean suc = response.getBoolean("success");
                                                if (suc) {
                                                    regisAlert = new AlertDialog.Builder(getActivity())
                                                            .setTitle("Register account")
                                                            .setMessage("Register successful")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                    regisAccount.setEnabled(true);
                                                                    regisDialog.dismiss();

                                                                }
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_info)
                                                            .show();

                                                }  else {
                                                    String res = response.getString("data");
                                                    if (res.matches("email_format")) {
                                                        failregisAlert = new AlertDialog.Builder(getActivity())
                                                                .setTitle("Email is wrong format")
                                                                .setMessage("Email is wrong format, please type correct format")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                        regisAccount.setEnabled(true);

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                                .show();
                                                    } else {
                                                        failregisAlert = new AlertDialog.Builder(getActivity())
                                                                .setTitle("Register account")
                                                                .setMessage("Register fail, please try again")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                        regisAccount.setEnabled(true);

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                                .show();
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), response.toString(), 500).show();

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity().getApplicationContext(), error.toString(), 500).show();

                            }
                        });

                        regisReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        VolleySingleton.getInstance(mContext).addToRequestQueue(regisReq);
                    } else {
                        // Password lenght < 4
                        AlertDialog passAlert = new AlertDialog.Builder(getActivity())
                                .setTitle("Password is too short")
                                .setMessage("Password is too short, please try another password")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        regisAccount.setEnabled(true);
                                        password.setText("");
                                        repassword.setText("");
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } else {
                    // Re password not match
                    AlertDialog passAlert = new AlertDialog.Builder(getActivity())
                            .setTitle("Password not match")
                            .setMessage("Password not match, please type the same password")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    regisAccount.setEnabled(true);
                                    repassword.setText("");
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        myDialog.show();
//

        login.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                final String email = emailLog.getText().toString();
                final String pass = passLog.getText().toString();
                String uri = String.format("http://10.0.2.2/coffee/public/login?email=%1$s&pass=%2$s",
                        email,
                        pass);

                JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                        uri,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("success")) {
                                    try {
                                        boolean suc = response.getBoolean("success");
                                        if (suc) {
                                            prefs.edit().putString("email", email).commit();
                                            prefs.edit().putString("pass", pass).commit();
                                            JSONObject jsonArray = response.getJSONObject("data");
                                            String name = jsonArray.getString("PS_EName");
                                            prefs.edit().putString("name", name).commit();
                                            myDialog.dismiss();
                                            changeStatus();
//                                            Toast.makeText(getActivity().getApplicationContext(), "Logged Successful: " + prefs.getString("name",""), 500).show();
                                        }  else {
                                            String data = response.getString("data");
                                            if (data.matches("no_account")) {
                                                AlertDialog alert = new AlertDialog.Builder(getActivity())
                                                        .setTitle("Login fail")
                                                        .setMessage("No this account")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            } else if (data.matches("email_format")) {
                                                AlertDialog alert = new AlertDialog.Builder(getActivity())
                                                        .setTitle("Email is wrong format")
                                                        .setMessage("Please type correct email")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            } else {
                                                AlertDialog alert = new AlertDialog.Builder(getActivity())
                                                        .setTitle("Log in fail")
                                                        .setMessage("Email or password is wrong")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), response.toString(), 500).show();

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Fail" + error.toString(), 500).show();

                    }
                });
                VolleySingleton.getInstance(mContext).addToRequestQueue(myReq);

            }
        });

        newAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                regisDialog.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regisDialog.dismiss();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }


}
