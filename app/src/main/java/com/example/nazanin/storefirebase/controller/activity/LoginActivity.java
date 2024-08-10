package com.example.nazanin.storefirebase.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.activity.admin.AllCategoriesActivity;
import com.example.nazanin.storefirebase.controller.activity.customer.ProductViewActivity;
import com.example.nazanin.storefirebase.controller.activity.customer.SignupActivity;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText,passwordEditText;
    private TextView wrongInfoTextView;
    private CheckBox keepMeLoggedinCheckbox;
    private boolean isLoggedin=false;
    private CustomerManager customerManager;
    private Customer customer;
    private Context context;
    private FirebaseAuth auth;
    public static boolean isAdmin;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        customerManager=new CustomerManager(context);
        customer=new Customer();
        //find
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        wrongInfoTextView=findViewById(R.id.wronginfo);
        wrongInfoTextView.setVisibility(View.INVISIBLE);
        keepMeLoggedinCheckbox=findViewById(R.id.keepLoggedin);
        auth = FirebaseAuth.getInstance();
        if (shouldBeKeptLoggedIn()){
            showLoginData();
        }

    }

    public void goToSignupForm(View view) {
        Intent intent=new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void checkForLoginData(View view) {


        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //admin
        if (emailEditText.getText().toString().equals("admin123@gmail.com") && passwordEditText.getText().toString().equals("admin123")){
            if (keepMeLoggedinCheckbox.isChecked()) {
                storeCustmerLoginData(email, password);
            }
            isAdmin=true;
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent categoriesIntent=new Intent(LoginActivity.this,AllCategoriesActivity.class);
                        startActivity(categoriesIntent);
                    }
                }
            });

        }
        //customer
        else {
            if (noFieldIsEmpty(email, password) && isEmailValid(email) && isPasswordValid(password)) {
//                if (customerManager.customerExist(email, password)) {
                    if (keepMeLoggedinCheckbox.isChecked()) {
                        storeCustmerLoginData(email, password);
                    }
                    //customer = customerManager.getThatCustomer();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                final Intent intent = new Intent(LoginActivity.this, ProductViewActivity.class);
                                CustomerManager manager = new CustomerManager(context);
                                manager.getCustomerByEmail(email, new OnSuccessListener<Customer>() {
                                    @Override
                                    public void onSuccess(Customer customer) {
                                        intent.putExtra("customer", customer);
                                        startActivity(intent);
                                    }
                                });

                            }
                            else {
                                String message = task.getException().getMessage();
                                String localizedMessage = task.getException().getLocalizedMessage();
                                wrongInfoTextView.setVisibility(View.VISIBLE);
                                wrongInfoTextView.setText(localizedMessage);
                            }
                        }
                    });

                } else {
                    wrongInfoTextView.setVisibility(View.VISIBLE);
                    wrongInfoTextView.setText("*اطلاعات وارد شده نادرست است*");
                }
            }
      //  }
    }
    private void storeCustmerLoginData(String email,String password){
        isLoggedin=true;
        SharedPreferences loginData=getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor=loginData.edit();
        editor.putBoolean("isLoggedin",isLoggedin);
        editor.putString("email",email);
        editor.putString("password",password);
        editor.apply();
    }

    private boolean shouldBeKeptLoggedIn(){
        SharedPreferences loginData=getSharedPreferences("loginData",MODE_PRIVATE);
        isLoggedin=loginData.getBoolean("isLoggedin",false);
        return isLoggedin;
    }

    private void showLoginData(){
        SharedPreferences loginData=getSharedPreferences("loginData",MODE_PRIVATE);
        emailEditText.setText(loginData.getString("email",null));
        passwordEditText.setText(loginData.getString("password",null));
        keepMeLoggedinCheckbox.setChecked(true);
    }

    private boolean isEmailValid(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("آدرس ایمیل وارد شده نامعتبر است");
            return false;
        }
        return true;
    }
    private boolean isPasswordValid(String password){
        if (password.length()<4) {
            passwordEditText.setError("طول رمز عبور حداقل چهار حرف است");
            return false;
        }
        return true;
    }

    private boolean noFieldIsEmpty(String email,String password){
        if (TextUtils.isEmpty(email)){
            emailEditText.setError("لطفا ایمیل خود را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(password)){
            passwordEditText.setError("لطفا رمز خود را وارد کنید");
            return false;
        }
        return true;
    }
}
