package com.example.rendezvous.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rendezvous.DB.RendezVousRepository;

public class AddViewModel extends AndroidViewModel {

    /**
     * ViewModel that handles the data flow between the MainActivity and its Fragment
     */

        private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

        private final RendezVousRepository repository;

        public AddViewModel(@NonNull Application application, RendezVousRepository repository) {
            super(application);
            this.repository = new RendezVousRepository(application);
        }

        /**
         * Method that set the image taken as a bitmap
         * @param bitmap the image taken by the user
         */
        public void setImageBitmap(Bitmap bitmap) {
            imageBitmap.setValue(bitmap);
        }

        public MutableLiveData<Bitmap> getImageBitmap() {
            return imageBitmap;
        }

        /*public void addCardItem(CardItem item) {
            repository.addCardItem(item);
        }*/
}


