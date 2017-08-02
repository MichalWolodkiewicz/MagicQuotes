package motivation.widget.android.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import motivation.widget.android.R;
import motivation.widget.android.model.quote.Quotes;
import motivation.widget.android.repository.QuotesRepositoryImpl;


public class FavouritesFragment extends Fragment {

    private ListView favouritesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.favourite_quotes_fragment, container, false);
        favouritesList = (ListView) fragmentView.findViewById(R.id.favourites_list);
        return fragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Quotes quotes = new QuotesRepositoryImpl(getActivity()).loadFavouritesQuotes();
            favouritesList.setAdapter(new FavouritesListAdapter(quotes, getActivity().getLayoutInflater()));
        }
    }

    private class FavouritesListAdapter implements ListAdapter {
        private Quotes quotes;
        private LayoutInflater layoutInflater;

        public FavouritesListAdapter(Quotes quotes, LayoutInflater layoutInflater) {
            this.quotes = quotes;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return quotes.count();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = layoutInflater.inflate(R.layout.favourite_quote_view, parent, false);
            }
            ((TextView)convertView.findViewById(R.id.quote)).setText(quotes.get(position).getText());
            ((TextView)convertView.findViewById(R.id.author)).setText(quotes.get(position).getAuthor());
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return quotes.count() < 1;
        }
    }
}
