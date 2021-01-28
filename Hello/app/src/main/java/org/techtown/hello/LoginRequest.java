package org.techtown.hello;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://106.10.53.84/login.php";
    private Map<String, String> parameters;

    LoginRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);
    }


    @Override
    public Map<String, String> getParams() { return parameters; }
}
