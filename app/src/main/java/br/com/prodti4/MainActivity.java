package br.com.prodti4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.firebase.firestore.FieldValue.serverTimestamp;

public class MainActivity extends AppCompatActivity {
    private static final String[] model = new String[]{ "Desktop","Notebook","Ultrabook","Fonte","Monitor","Teclado","Mouse","HD","Memória"};
    private static final String[] SISTEMAS = new String[]{"Windows 10","Windows 7","Windows XP","Linux","Outros"};
    private static final String[] tec = new String[]{"Fulano","Fulano2","Fulano3","Fulano4", "Fulano5"};          // Opção de escolhas para completar
    private static final String[] serv = new String[]{"Teste","Instalação do sistema","Upgrade de memória para 8GB","Limpeza","Instalação de programas","Troca de HD",
            "Ativação Windows","Limpeza do cooler","Troca de Carcaça","Limpeza da fonte","Troca da fonte","Instalação do driver","Troca bateria","Troca de memória","Limpeza da memória","Upgrade de memória para 12GB","Troca de HD para SSD"};

    AutoCompleteTextView sistemaOperacional,modelo,tecnico;
    MultiAutoCompleteTextView servico;
    EditText patrimonio;
    Button salvar, cancelar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    //Progress dialog
    ProgressDialog pd;

    //Firestore instance
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PRODTI");


        //initialize views with its xml
        patrimonio = findViewById(R.id.patrimonio);
        modelo = findViewById(R.id.modelo);
        servico = findViewById(R.id.servico);
        sistemaOperacional = findViewById(R.id.sistemaOperacional);
        tecnico = findViewById(R.id.tecnico);

        salvar = findViewById(R.id.salvar);
        cancelar = findViewById(R.id.cancelar);

        {
            AutoCompleteTextView textView = findViewById(R.id.sistemaOperacional);                              // PROGRAMA AS OPÇÕES QUE do autocomplete
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SISTEMAS);
            textView.setAdapter(adapter);


            textView = findViewById(R.id.tecnico);
           ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tec);
            textView.setAdapter(adapter1);


            textView = findViewById(R.id.modelo);
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, model);
            textView.setAdapter(adapter3);

        }
        {
            MultiAutoCompleteTextView textView = findViewById(R.id.servico);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, serv);
            textView.setAdapter(adapter2);
            textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


    }

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patrimonio.setText("");
                modelo.setText("");
                servico.setText("");
                sistemaOperacional.setText("");
                tecnico.setText("");
            }
        });

        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click Button to upload data
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("CSI");

                String Patrimonio = patrimonio.getText().toString();
                String Modelo = modelo.getText().toString();
                String Servico = servico.getText().toString();
                String Sistemaoperacional = sistemaOperacional.getText().toString();
                String Tecnico = tecnico.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String data = ref.push().getKey();
                Map<String, Object> value = new HashMap<>();
                value.put("timestamp", ServerValue.TIMESTAMP);

                CadastroProdti helperClass = new CadastroProdti(Patrimonio, Modelo, Servico, Sistemaoperacional, Tecnico, value);

                reference.push().setValue(helperClass,ServerValue.TIMESTAMP);

                patrimonio.setText("");
                modelo.setText("");
                servico.setText("");
                sistemaOperacional.setText("");
                tecnico.setText("");

                //funcion call to uploat data
                uploadData(Patrimonio, Modelo, Servico, Sistemaoperacional, Tecnico);
            }

            private <DocumentReference> void uploadData(String patrimonio, String modelo, String Servico, String Sistemaoperacional, String Tecnico) {
                //set title of progress bar
                pd.setTitle("SALVANDO...");
                //show progress bar when user click save button.
                pd.show();
                //ramdom id for each data to be stored
                final String id = UUID.randomUUID().toString();

                final Map<String, Object> doc = new HashMap<>();
                Map<String, Object> updates = new HashMap<>();
                doc.put("id", id); //id for data
                doc.put("patrimonio", patrimonio);
                doc.put("modelo", modelo);
                doc.put("Servico", Servico);
                doc.put("sistema_operacional", Sistemaoperacional);
                doc.put(" Tecnico", Tecnico);
                doc.put("Data", serverTimestamp());
                //add this data
                db.collection("Banco Prodti").document(id).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //this will be called when data is added successfully

                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "SALVO", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //this will be called if there is any error while uploading
                                                pd.dismiss();
                                                // get and show error message
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    y       }
                                        }
                );
            }
        });
    }
}


//OBS: ESTE É A AÇÃO PARA PULAR LINHA QUANDO CLICAR EM NEXT NO ACTIVITY_MAIN Layout É ESTE DAQUI --------->> actioandroid:imeOptions="actionNext"













