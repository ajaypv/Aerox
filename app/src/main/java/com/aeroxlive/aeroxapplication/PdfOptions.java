package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfOptions extends AppCompatActivity {


    public PDFView pdfView;
    ImageButton copyAdd,copyMinus;
    EditText printCopies,pageRanage;
    Spinner ColorOption,sideOption,binnding,ColorBindding,PangeRange;
    LinearLayout printRangesLayout;

    Button printPreview ,printFile;


    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_options);

        copyMinus = findViewById(R.id.coppiesMinus);
        printPreview = findViewById(R.id.button_preview);
        copyAdd = findViewById(R.id.coppiesPlus);
        printCopies = findViewById(R.id.coppiesText);
        printRangesLayout = findViewById(R.id.layoutpageRange);
        printFile = findViewById(R.id.print_file);

        copyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (printCopies.getText() != null) {
                        int count = Integer.parseInt(printCopies.getText().toString());
                        printCopies.setText(String.valueOf(count + 1));
                    }
                }catch (Exception e){
                    Toast.makeText(PdfOptions.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        copyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (printCopies.getText() != null) {
                        int count = Integer.parseInt(printCopies.getText().toString());
                        if (count <= 0) {
                            Toast.makeText(PdfOptions.this, "Can not be less Then Zero", Toast.LENGTH_SHORT).show();
                        } else {
                            printCopies.setText(String.valueOf(count - 1));
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(PdfOptions.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ColorOption = findViewById(R.id.spinnerColourChoose);
        sideOption = findViewById(R.id.spinnerSidesSelecet);
        binnding = findViewById(R.id.spinnerBinding);
        ColorBindding = findViewById(R.id.spinnerSidesSelecetBindingColor);
        pageRanage = findViewById(R.id.pagesRange);
        PangeRange= findViewById(R.id.spinnerSidesSelecetPageRange);




   






        Intent intent3 = getIntent();
        String pdfName = intent3.getStringExtra("pdfName");
        int TotalPages = Integer.parseInt(intent3.getStringExtra("numberPages"));
        String folderName = "privatePdfs" + File.separator;
        File file = new File(getExternalFilesDir(folderName), pdfName);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        pdfView.fromFile(file)
                .swipeHorizontal(true)
                .spacing(20) // Set spacing between pages to 20
                .pageFitPolicy(FitPolicy.HEIGHT)
                .load();

        pdfView.setBackgroundColor(getResources().getColor(R.color.transparent));


// Adjust the padding
        pdfView.setPadding(20, 20, 20, 20);
        pdfView.zoomTo(2.0f);

// Set the zoom level of the other pages to 0.3 (30% view)
        PangeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String customPages = adapterView.getSelectedItem().toString();
                if(customPages.equals("Custom")){
                    printRangesLayout.setVisibility(View.VISIBLE);
                }else{
                    printRangesLayout.setVisibility(View.GONE);
                    pdfView.fromFile(file)
                            .swipeHorizontal(true)
                            .spacing(20) // Set spacing between pages to 20
                            .pageFitPolicy(FitPolicy.HEIGHT)
                            .load();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        printPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displaypages = pageRanage.getText().toString();
                String pageRanges = displaypages;

                if (isValidPageRanges(displaypages, TotalPages)) {
                    List<Integer> pages = new ArrayList<>();
                    String[] rangeArray = pageRanges.split(",");
                    for (String range : rangeArray) {
                        String[] rangeParts = range.split("-");
                        if (rangeParts.length == 1) {
                            // Single page
                            int page = Integer.parseInt(rangeParts[0]);
                            pages.add(page);
                        } else if (rangeParts.length == 2) {
                            // Page range
                            int startPage = Integer.parseInt(rangeParts[0]);
                            int endPage = Integer.parseInt(rangeParts[1]);
                            for (int page = startPage; page <= endPage; page++) {
                                pages.add(page);
                            }
                        } else {
                            Toast.makeText(PdfOptions.this, "Invalid page range: " + range, Toast.LENGTH_SHORT).show();
                            throw new IllegalArgumentException("Invalid page range: " + range);

                        }
                    }

                    // Subtract 1 from each page number in the list to adjust the indexing
                    for (int i = 0; i < pages.size(); i++) {
                        pages.set(i, pages.get(i) - 1);
                    }

                    int[] pp = new int[pages.size()];
                    for (int i = 0; i < pages.size(); i++) {
                        pp[i] = pages.get(i);
                    }
                    System.out.println(pages);

                    pdfView.fromFile(file)
                            .pages(pp)
                            .swipeHorizontal(true)
                            .spacing(20) // Set spacing between pages to 20
                            .pageFitPolicy(FitPolicy.HEIGHT)
                            .load();
                }
                else{
                    Toast.makeText(PdfOptions.this, "Enter a valid Page Range", Toast.LENGTH_SHORT).show();
                }

                

            }
        });





    printFile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String color = ColorOption.getSelectedItem().toString();
            String sides = sideOption.getSelectedItem().toString();
            String binding = binnding.getSelectedItem().toString();
            String bindingColor = ColorBindding.getSelectedItem().toString();
            String PageRanage = pageRanage.getText().toString();
            String customSelect = PangeRange.getSelectedItem().toString();
            int Numberofcopies = Integer.parseInt(printCopies.getText().toString());
            if(Numberofcopies <= 0){
                Toast.makeText(PdfOptions.this, "Can not Zero prints", Toast.LENGTH_SHORT).show();
                
            }else{

                if(customSelect.equals("Custom")){
                    if(isValidPageRanges(PageRanage,TotalPages)){

                        int customPagesCount =  getNumPagesInRange(PageRanage);
                        System.out.println("------>"+customPagesCount);
                        Intent confirm = new Intent(getApplicationContext(),PaymentPage.class);
                        confirm.putExtras(intent3.getExtras());
                        confirm.putExtra("selected_color", color);
                        confirm.putExtra("customPagesCount",String.valueOf(customPagesCount));
                        confirm.putExtra("num_copies", String.valueOf(Numberofcopies));
                        confirm.putExtra("all_pages_selected", customSelect);
                        confirm.putExtra("custom_page_range", PageRanage);
                        confirm.putExtra("selected_binding", binding);
                        confirm.putExtra("print_sides",sides);
                        confirm.putExtra("bindingColor",bindingColor);
                        startActivity(confirm);

                    }else{
                        Toast.makeText(PdfOptions.this, "Enter a valid Page Range", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent confirm = new Intent(getApplicationContext(),PaymentPage.class);
                    confirm.putExtras(intent3.getExtras());
                    confirm.putExtra("selected_color", color);
                    confirm.putExtra("num_copies", String.valueOf(Numberofcopies));
                    confirm.putExtra("all_pages_selected", customSelect);
                    confirm.putExtra("custom_page_range", PageRanage);
                    confirm.putExtra("selected_binding", binding);
                    confirm.putExtra("print_sides",sides);
                    confirm.putExtra("bindingColor",bindingColor);
                    startActivity(confirm);

                }

            }



        }
    });





    }

    public int getNumPagesInRange(String input) {
        int numPages = 0;
        String[] ranges = input.split(",");
        for (String range : ranges) {
            String[] pages = range.split("-");
            int startPage, endPage;
            if (pages.length == 1 && !pages[0].isEmpty()) {
                startPage = endPage = Integer.parseInt(pages[0]);
                numPages += 1;
            } else if (pages.length == 2 && !pages[0].isEmpty() && !pages[1].isEmpty()) {
                startPage = Integer.parseInt(pages[0]);
                endPage = Integer.parseInt(pages[1]);
                numPages += (endPage - startPage) + 1;
            }
        }
        return numPages;
    }




    public boolean isValidPageRanges(String input, int maxPage) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            String[] ranges = input.split(",");
            for (String range : ranges) {
                String[] pages = range.split("-");
                int startPage, endPage;
                if (pages.length == 1 && !pages[0].isEmpty()) {
                    startPage = endPage = Integer.parseInt(pages[0]);
                } else if (pages.length == 2 && !pages[0].isEmpty() && !pages[1].isEmpty()) {
                    startPage = Integer.parseInt(pages[0]);
                    endPage = Integer.parseInt(pages[1]);
                } else {
                    return false; // Invalid range
                }
                if (startPage > endPage || startPage < 1 || endPage > maxPage) {
                    return false; // Invalid range
                }
            }
            return true; // All ranges are valid
        }catch (Exception e){
            return false;
        }
    }


}
