package com.example.waive.ui.activity;

import com.joanzapata.pdfview.PDFView;
import com.example.waive.R;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TermsActivity extends Activity {

	private ImageButton mBackButton = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        
        mBackButton = (ImageButton)findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TermsActivity.this.finish();
			}
		});
        
        PDFView pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.fromAsset("Terms.pdf")
	    .pages(0, 2, 1, 3, 3, 3)
	    .defaultPage(1)
	    .showMinimap(false)
	    .enableSwipe(true)
	    .swipeVertical(true)
	    .load();
    }
}
