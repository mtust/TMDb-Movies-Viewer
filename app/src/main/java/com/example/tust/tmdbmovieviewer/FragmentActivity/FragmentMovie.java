package com.example.tust.tmdbmovieviewer.FragmentActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.tust.tmdbmovieviewer.Adapter.MovieTMDbAdapter;
import com.example.tust.tmdbmovieviewer.R;
import com.example.tust.tmdbmovieviewer.api.MovieDBClient;
import com.example.tust.tmdbmovieviewer.api.WebServiceClient;
import com.example.tust.tmdbmovieviewer.model.Request.WebServiceRequest;
import com.example.tust.tmdbmovieviewer.model.Response.MoviesResponse;

import java.util.HashMap;
import java.util.Map;

public class FragmentMovie extends SherlockFragment {
   private WebServiceClient client = MovieDBClient.getInstance();
   private static FragmentMovie[] fragmentMovie = {new FragmentMovie(0), new FragmentMovie(1), new FragmentMovie(2)};

   private ListView moviesListView;
   private MovieTMDbAdapter moviesAdapter;
   private String[] defaultRequestUrls = {"/3/movie/popular","/3/movie/top_rated","/3/movie/upcoming"};
   int position;

    private MoviesResponse movies;

    static public FragmentMovie getFm(int position){
        return fragmentMovie[position];
    }

    public FragmentMovie(int position){
        super();
        this.position = position;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fraqListView = inflater.inflate(R.layout.activity_main, container, false);
        moviesListView = (ListView) getActivity().findViewById(R.id.moviesListView);
        return fraqListView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
            return;
        //   new Thread(new Runnable() {
        //        @Override
        //  public void run() {

        movies = getInitialMovies();
        moviesAdapter = new MovieTMDbAdapter(getActivity().getApplicationContext(), movies, defaultRequestUrls[position], client);



        moviesListView.setAdapter(moviesAdapter);

         //     }
       // }).start();

        moviesListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (totalItemCount > 0) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if ((lastInScreen == totalItemCount) && (lastInScreen != movies.getTotalResults())) {
                        Log.i("ListView", "End");
                        moviesAdapter.nextPage();
                        moviesAdapter.updateMovies();
                        moviesAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }


    private MoviesResponse getInitialMovies() {
        Map<String, String> parameters =  new HashMap<String, String>();



        WebServiceRequest request = new WebServiceRequest();
        request.setHost(MovieDBClient.HOST);
        request.setParameters(parameters);
        request.setRequestUrl(defaultRequestUrls[position]);

        MoviesResponse movies = client.sendRequest(request, MoviesResponse.class);
        Log.i("Movies", movies.toString());
        return movies;
    }
}
