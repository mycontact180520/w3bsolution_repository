package com.w3bsolution.entrega2s;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class CartActivity extends AppCompatActivity {

    TabHost purchase_tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

       /* //personalizando los taps de la compra
        purchase_tabs = (TabHost) findViewById(R.id.purchase_tabs);
        purchase_tabs.setup();

        TabHost.TabSpec spec = purchase_tabs.newTabSpec("tag1");
        //creando envio tab
        spec.setIndicator("Envío");
        spec.setContent(R.id.tab1);
        purchase_tabs.addTab(spec);


        //creando pago tab
        spec = purchase_tabs.newTabSpec("tag2");
        spec.setIndicator("Pago");
        spec.setContent(R.id.tab2);
        purchase_tabs.addTab(spec);

        //creando confirmar tab
        spec = purchase_tabs.newTabSpec("tag3");
        spec.setIndicator("Confirmar");
        spec.setContent(R.id.tab3);
        purchase_tabs.addTab(spec);*/
    }
}
