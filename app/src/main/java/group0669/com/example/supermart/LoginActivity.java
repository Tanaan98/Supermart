package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.User;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonLogin;
    EditText editUserName, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserName = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonLogin:
                // get the data from the username and password to see if its correct
                try{
                    // get user type from its id
                    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
                    User user = sel.getUser(Integer.parseInt(editUserName.getText().toString()));

                    Intent intent = new Intent(this, MainActivity.class);
                    if(sel.getRoleName(user.getRoleId()).equals("ADMIN")){
                        intent = new Intent(this, Admin.class);
                    } else if(sel.getRoleName(user.getRoleId()).equals("CUSTOMER")){
                        intent = new Intent(this, Customer.class);
                    }
                    startActivity(intent);
                    break;
                } catch (InvalidRoleException e) {
                    e.printStackTrace();
                } catch (InvalidIdException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }
    }
}
