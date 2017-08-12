package motivation.widget.android.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    private RecyclerView favouritesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.favourite_quotes_fragment, container, false);
        favouritesList = (RecyclerView) fragmentView.findViewById(R.id.favourites_list);
        favouritesList.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favouritesList.setLayoutManager(mLayoutManager);
        itemTouchHelper.attachToRecyclerView(favouritesList);
        return fragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Quotes quotes = new QuotesRepositoryImpl(getActivity()).loadFavouritesQuotes();
            favouritesList.setAdapter(new FavouritesListAdapter(quotes));
            favouritesList.invalidate();
        }
    }

    private class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> {
        private Quotes quotes;

        public FavouritesListAdapter(Quotes quotes) {
            this.quotes = quotes;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return quotes.count();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_quote_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.quote.setText(quotes.get(position).getText());
            holder.author.setText(quotes.get(position).getAuthor());
            holder.index = quotes.get(position).getIndex();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView quote;
            private TextView author;
            private int index;

            public ViewHolder(View itemView) {
                super(itemView);
                this.quote = (TextView) itemView.findViewById(R.id.quote);
                this.author = (TextView) itemView.findViewById(R.id.author);
            }
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //((FavouritesListAdapter.ViewHolder)viewHolder).index;
        }
    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
}
