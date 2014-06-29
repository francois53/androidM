package com.example.webview;

import com.example.webview.R;
import com.parse.ParseAnalytics;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import com.google.android.gms.ads.*;

public class MainActivity extends Activity {
	private WebView webView;
	private InterstitialAd interstitial;
	  ProgressBar loadingProgressBar,loadingTitle;
	
@Override
   public void onCreate(Bundle savedInstanceState)  {
       super.onCreate(savedInstanceState);
    
       setContentView(R.layout.activity_main);
       AdRequest adRequest = new AdRequest.Builder().build();
       interstitial = new InterstitialAd(this);
       interstitial.setAdUnitId(getString(R.string.ad_inter));
       AdView adView = (AdView)this.findViewById(R.id.adView);
       adView.loadAd(adRequest);
       interstitial.loadAd(adRequest);
   	   ParseAnalytics.trackAppOpened(getIntent());
     webView = (WebView) findViewById(R.id.webView);
     webView.getSettings().setJavaScriptEnabled(true);
     webView.loadUrl(getString(R.string.url_webview));
     webView.setWebViewClient(new WebViewClient());
     webView.setEnabled(true);  
     webView.getSettings().setAllowFileAccess(true);
     webView.setWebChromeClient(new WebChromeClient());
     webView.getSettings().setLoadWithOverviewMode(true);
     webView.getSettings().setUseWideViewPort(true);
     webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
     webView.getSettings().setBuiltInZoomControls(true);
     webView.getSettings().setSupportZoom(true); 
     webView.setWebViewClient(new WebViewClient()
     {
    	 @SuppressWarnings("unused")
		public boolean shouldOverrideUrlLoading(webview view, String url){
			return false;
    	    
         }
    	 public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
 	        webView.loadDataWithBaseURL(null, getString(R.string.html_error), "text/html", "utf-8", null);
 	       Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();       
    	 }
    	 
    	 
    	 @Override
    	    public boolean shouldOverrideUrlLoading(WebView view, String url)
    	    {
    	        if (url.endsWith(".pdf")
    	        	|| url.endsWith(".txt")
                    || url.endsWith(".doc")
                    || url.endsWith(".docx")
                    || url.endsWith(".xls")
                    || url.endsWith(".xlsx")
                    || url.endsWith(".ppt")
                    || url.endsWith(".pptx")
                    || url.endsWith(".pages")
                    || url.endsWith(".ai")
                    || url.endsWith(".psd")
                    || url.endsWith(".tiff")
                    || url.endsWith(".dxf")
                    || url.endsWith(".svg")
                    || url.endsWith(".eps")
                    || url.endsWith(".ps")
                    || url.endsWith(".ttf")
                    || url.endsWith(".xps")){
    	           String googleDocs = "https://docs.google.com/viewer?url="; 
    	           webView.loadUrl(googleDocs + url);

    	        }
    	        else
    	        {
    	            view.loadUrl(url);
    	        }

    	        return true;
    	        
    	     }
    	 

         @Override
         public void onPageFinished(final WebView view, String url) {
             String javascript = "javascript:" +
                 "var iframes = document.getElementsByTagName('iframe');" +
                 "for (var i = 0, l = iframes.length; i < l; i++) {" +
                 "   var iframe = iframes[i]," +
                 "   a = document.createElement('a');" +
                 "   a.setAttribute('href', iframe.src);" +
                 "   d = document.createElement('div');" +
                 "   d.style.width = iframe.offsetWidth + 'px';" +
                 "   d.style.height = iframe.offsetHeight + 'px';" +
                 "   d.style.top = iframe.offsetTop + 'px';" +
                 "   d.style.left = iframe.offsetLeft + 'px';" +
                 "   d.style.position = 'absolute';" +
                 "   d.style.opacity = '0';" +
                 "   d.style.filter = 'alpha(opacity=0)';" +
                 "   d.style.background = 'black';" +
                 "   a.appendChild(d);" +
                 "   iframe.offsetParent.appendChild(a);" +
                 "}";
             view.loadUrl(javascript);
             
             super.onPageFinished(view, url);
         }
     });

     loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal); 
     
     webView.setWebChromeClient(new WebChromeClient() {

     public void onProgressChanged(WebView view, int newProgress) {

     super.onProgressChanged(view, newProgress);


     loadingProgressBar.setProgress(newProgress);

     if (newProgress == 100) {
     loadingProgressBar.setVisibility(View.GONE);

     
     } else{
     loadingProgressBar.setVisibility(View.VISIBLE);

     
     }
     }

     });

       } 

   @Override
   public void onBackPressed()
   {
       if(webView.canGoBack()){
           webView.goBack();
       }else{
           new AlertDialog.Builder(this)
           .setIcon(android.R.drawable.ic_dialog_alert)
           .setTitle(getString(R.string.menu_title))
           .setMessage(getString(R.string.menu_text))
           .setPositiveButton(getString(R.string.menu_ok), new DialogInterface.OnClickListener()
           {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   finish();    
                   if (interstitial.isLoaded()) {
                	      interstitial.show();
                	    }
               }

           })
           .setNegativeButton(getString(R.string.menu_nook), null)
           .show();    
       }   
   }
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_home){
			webView.loadUrl(getString(R.string.url_webview));
            return true;
        }
        if(item.getItemId() == R.id.action_rate){
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse("market://details?id="+getString(R.string.package_name)+""));
        	startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.action_facebook){
        	try {
     		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+getString(R.string.fb_id)+"")));
        	}catch (Exception e) {
     		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+getString(R.string.fb_id)+"")));
        	}
            return true;
        }
        if(item.getItemId() == R.id.action_twitter){
        	try {
        		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+getString(R.string.twitter_username)+"")));
        		}catch (Exception e) {
        		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/"+getString(R.string.twitter_username)+"")));
        		}
            return true;
        }
        if(item.getItemId() == R.id.action_about){
        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        	alertDialog.setTitle(getString(R.string.about_title));
        	alertDialog.setMessage(getString(R.string.about_text));
        	alertDialog.setButton(getString(R.string.about_button), new DialogInterface.OnClickListener() {
        	public void onClick(final DialogInterface dialog, final int which) {
        	// here you can add functions
        	}
        	});
        	alertDialog.setIcon(R.drawable.ic_launcher);
        	alertDialog.show();
            return true;
        }
        if(item.getItemId() == R.id.menu_item_share){
        	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        	sharingIntent.setType("text/plain");
        	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text));
        	startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_title)));
            return true;
        }
        return super.onOptionsItemSelected(item);
        
	
}
	
}
