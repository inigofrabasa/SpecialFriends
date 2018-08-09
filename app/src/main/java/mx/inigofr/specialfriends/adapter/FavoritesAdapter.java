package mx.inigofr.specialfriends.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.inigofr.specialfriends.R;
import mx.inigofr.specialfriends.model.UserModel;

/**
 * Created by inigo on 09/08/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteRecyclerViewHolder> {
    private List<UserModel> userModelList;
    private View.OnClickListener clickListener;

    public FavoritesAdapter(List<UserModel> userModelList, View.OnClickListener clickListener) {
        this.userModelList = userModelList;
        this.clickListener = clickListener;
    }

    @Override
    public FavoritesAdapter.FavoriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoritesAdapter.FavoriteRecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.FavoriteRecyclerViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

        holder.nameTextView.setText(userModel.get_personName());
        //if(userModel.get_birthday() != null)
        //    holder.dateTextView.setText(userModel.get_birthday().toString().substring(0, 11));
        //if(userModel.get_imageBitmap() != null)
        //    holder.circleImageProfilePicture.setImageBitmap(userModel.get_imageBitmap());
        holder.itemView.setTag(userModel);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void addItems(List<UserModel> inUserModelList) {
        this.userModelList = new ArrayList<UserModel>();
        for (UserModel model : inUserModelList){
            if(model.get_isFavorite()){
                this.userModelList.add(model);
            }
        }

        notifyDataSetChanged();
    }

    public class FavoriteRecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView;
        private CircleImageView circleImageProfilePicture;

        public FavoriteRecyclerViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            circleImageProfilePicture = (CircleImageView)view.findViewById(R.id.circleImageProfilePicture);
        }
    }
}
