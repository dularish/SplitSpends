package dularish.splitspends;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuthref;
    EditText emailidEditText;
    EditText passwordEditText;
    Button registerButton;
    TextView signInTextView;
    TextView offlineModeTextView;
    String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuthref = FirebaseAuth.getInstance();
        emailidEditText = (EditText) findViewById(R.id.useremailEditText);
        passwordEditText = (EditText) findViewById(R.id.userpasswordEditText);
        registerButton = (Button) findViewById(R.id.registeruserbutton);
        signInTextView = (TextView) findViewById(R.id.textviewalreadyanusersignin);
        offlineModeTextView = (TextView) findViewById(R.id.textviewofflineversion);

        registerButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
        offlineModeTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == registerButton){
            if(TextUtils.isEmpty(emailidEditText.getText().toString().trim())){
                Toast toast = Toast.makeText(getApplicationContext(),"Email Id is empty",Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(TextUtils.isEmpty(passwordEditText.getText().toString().trim())){
                Toast toast = Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try{
                firebaseAuthref.createUserWithEmailAndPassword(emailidEditText.getText().toString().trim(), passwordEditText.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast  = Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
            catch (Exception ex){
                Log.e(TAG,ex.getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT);
                toast.show();

            }
            //Toast toast = Toast.makeText(getApplicationContext(),"Registered successfully",Toast.LENGTH_SHORT);
            //toast.show();
        }
        else if(v == signInTextView){
            Intent intent = new Intent(getApplicationContext(),SplitSpendsListActivity.class);
            startActivity(intent);
        }
        else if(v == offlineModeTextView){

        }
    }
}
