package com.example.tust.tmdbmovieviewer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tust.tmdbmovieviewer.R;
import com.example.tust.tmdbmovieviewer.api.MovieDBClient;
import com.example.tust.tmdbmovieviewer.api.WebServiceClient;
import com.example.tust.tmdbmovieviewer.model.Request.WebServiceRequest;
import com.example.tust.tmdbmovieviewer.model.Response.Movie;
import com.example.tust.tmdbmovieviewer.model.Response.MoviesResponse;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieTMDbAdapter extends BaseAdapter {

    private MoviesResponse movies;
    private List<Movie> movieslist;
    private LayoutInflater inflater;
    private Context context;
    private String requestUrl;
    private WebServiceClient client;


    public MovieTMDbAdapter(Context context, MoviesResponse movies, String requestUrl, WebServiceClient client) {
        super();
        this.movies = movies;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.requestUrl = requestUrl;
        this.client = client;
        movieslist = movies.getResults();
    }

    @Override
    public int getCount() {
        return movies.getResults().size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.getResults().get(position);
    }

    @Override
    public long getItemId(int position) {
        return movies.getResults().get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_list_item, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String description = MessageFormat.format("Rate: {0}\nPopularity: {1}\nDate: {2}",
                movies.getResults().get(position).getVoteAverage(),
                movies.getResults().get(position).getPopularity(),
                movies.getResults().get(position).getDate());

        viewHolder.title = detail(view, R.id.listItemTitle, movies.getResults().get(position).getTitle());
        viewHolder.description = detail(view, R.id.listitemDescription, description);



        return view;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }


    private class ViewHolder {
        TextView title;
        TextView description;
        ImageView icon;
    }


    public void nextPage() {
        Integer page = movies.getPage() + 1;
        movies.setPage(page);
    }


    public void updateMovies() {
        Map<String, String> parameters =  new HashMap<String, String>();
        parameters.put("page", String.valueOf(movies.getPage()));

        WebServiceRequest request = new WebServiceRequest();
        request.setHost(MovieDBClient.HOST);
        request.setParameters(parameters);
        request.setRequestUrl(requestUrl);

        MoviesResponse movies = client.sendRequest(request, MoviesResponse.class);
        Log.i("Movies", movies.toString());

        movieslist.addAll(movies.getResults());

        this.movies = movies;
        this.movies.setResults(movieslist);
    }
}
