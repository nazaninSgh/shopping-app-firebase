package com.example.nazanin.store.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.nazanin.store.R;
import com.example.nazanin.store.model.DAO.CustomerManager;
import com.example.nazanin.store.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText,lastnameEditText,emailEditText,passwordEditText,repeatPasswordEditText;
    private CustomerManager customerManager;
    private Context context;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context=this;
        customerManager=new CustomerManager(context);

        //find
        nameEditText=findViewById(R.id.name);
        lastnameEditText=findViewById(R.id.lastname);
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        repeatPasswordEditText=findViewById(R.id.passwordAgain);
    }


    public void signupConfirmation(View view) {
        Customer customer=new Customer();
        customer.setName(nameEditText.getText().toString());
        customer.setLastname(lastnameEditText.getText().toString());
        customer.setEmail(emailEditText.getText().toString());
        customer.setPassword(passwordEditText.getText().toString());
        String repeatpassword=repeatPasswordEditText.getText().toString();

        if (noFieldIsEmpty(customer,repeatpassword) && isEmailValid(customer) && isPasswordValid(customer) && passwordDoubleCheckTrue(customer,repeatpassword)) {
            if (customerManager.insertCustomerData(customer)) {
                customer.setId(customerManager.getCustomerId(customer));
                Intent intent = new Intent(this, ProductViewActivity.class);
                intent.putExtra("customer", customer);
                startActivity(intent);
                finish();
            }
            else {
                emailEditText.setError("آدرس ایمیل وارد شده تکراری است");
            }

        }
    }

    private boolean isEmailValid(Customer customer){
        if (!Patterns.EMAIL_ADDRESS.matcher(customer.getEmail()).matches()){
            emailEditText.setError("آدرس ایمیل وارد شده نامعتبر است");
            return false;
        }
        return true;
    }
    private boolean isPasswordValid(Customer customer){
        if (customer.getPassword().length()<4) {
            passwordEditText.setError("طول رمز عبور حداقل چهار حرف است");
            return false;
        }
        return true;
    }

    private boolean noFieldIsEmpty(Customer customer,String repeatPassword){
        if (TextUtils.isEmpty(customer.getName())){
            nameEditText.setError("لطفا نام خود را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(customer.getLastname())){
            lastnameEditText.setError("لطفا نام خانوادگی خود را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(customer.getEmail())){
            emailEditText.setError("لطفا ایمیل خود را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(customer.getPassword())){
            passwordEditText.setError("لطفا رمز خود را انتخاب کنید");
            return false;
        }
        if (TextUtils.isEmpty(repeatPassword)){
            repeatPasswordEditText.setError("لطفا رمز خود را تکرار کنید");
            return false;
        }
        return true;
    }
    private boolean passwordDoubleCheckTrue(Customer customer,String repeatPassword){
        if (customer.getPassword().equals(repeatPassword))
            return true;
        else {
            repeatPasswordEditText.setError("رمز عبور تکرار شده نادرست است");
            return false;
        }
    }
}
