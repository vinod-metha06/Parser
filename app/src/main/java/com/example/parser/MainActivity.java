package com.example.parser;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    Button parseXmlBtn, parseJsonBtn;
    TextView displayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseJsonBtn = findViewById(R.id.parseJsonBtn);
        parseXmlBtn = findViewById(R.id.parseXmlBtn);
        displayTextView = findViewById(R.id.displayTextView);
        parseXmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream is = getAssets().open("student.xml");
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(is);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("XML Data");
                    stringBuilder.append("\n----------");
                    NodeList nodeList = document.getElementsByTagName("student");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            stringBuilder.append("\nName: ").append(getValue("name", element));
                            stringBuilder.append("\nUSN: ").append(getValue("usn", element));
                            stringBuilder.append("\n----------");
                        }
                    }
                    displayTextView.setText(stringBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error Parsing XML", Toast.LENGTH_SHORT).show();
                }
            }
        });
        parseJsonBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String json;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    InputStream is = getAssets().open("student.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    json = new String(buffer, StandardCharsets.UTF_8);
                    JSONArray jsonArray = new JSONArray(json);
                    stringBuilder.append("Students");
                    stringBuilder.append("\n----------");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        stringBuilder.append("\nName: ").append(jsonObject.getString("name"));
                        stringBuilder.append("\nUSN: ").append(jsonObject.getString("usn"));

                        stringBuilder.append("\n----------");
                    }
                    displayTextView.setText(stringBuilder.toString());
                    is.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error in parsing JSON data from!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String getValue(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
    }
}