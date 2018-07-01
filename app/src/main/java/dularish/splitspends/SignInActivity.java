package dularish.splitspends;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuthRef;
    Button SignInButton;
    EditText userEmail;
    EditText userPassword;
    String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuthRef = FirebaseAuth.getInstance();
        SignInButton = (Button) findViewById(R.id.signinuserbutton);
        userEmail = (EditText) findViewById(R.id.useremailSignInPageEditText);
        userPassword = (EditText) findViewById(R.id.userpasswordSignInPageEditText);

        SignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == SignInButton){
            try{
                if(TextUtils.isEmpty(userEmail.getText().toString().trim())){
                    Toast toast = Toast.makeText(getApplicationContext(),"User Email is Empty", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(TextUtils.isEmpty(userPassword.getText().toString().trim())){
                    Toast toast = Toast.makeText(getApplicationContext(),"User Password is Empty", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    firebaseAuthRef.signInWithEmailAndPassword(userEmail.getText().toString().trim(),userPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast toast = Toast.makeText(getApplicationContext(),"User Logged In Successfully", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else{
                                        Toast toast = Toast.makeText(getApplicationContext(),"User Credentials are incorrect", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            });
                }
            }
            catch (Exception ex){
                Log.e(TAG,ex.getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),"Some error occurred, try again", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
