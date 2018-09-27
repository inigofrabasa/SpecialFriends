package mx.inigofr.specialfriends.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.inigofr.specialfriends.R;
import mx.inigofr.specialfriends.model.UserModel;

/**
 * Created by inigo on 08/08/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel> userModelList;
    private View.OnClickListener clickListener;
    private Context context;

    public UsersAdapter(List<UserModel> userModelList, View.OnClickListener clickListener, Context context) {
        this.userModelList = userModelList;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        UserModel userModel = userModelList.get(position);
        if(!userModel.is_isFirst()){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            return new RecyclerViewHolderItem(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item, parent, false));}

        return new RecyclerViewHolderHeader(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

        if(!userModel.is_isFirst()){
            ((RecyclerViewHolderItem)holder).nameTextView.setText(userModel.get_personName());
            //if(userModel.get_birthday() != null)
            //    holder.dateTextView.setText(userModel.get_birthday().toString().substring(0, 11));
            //if(userModel.get_imageBitmap() != null)
            //    holder.circleImageProfilePicture.setImageBitmap(userModel.get_imageBitmap());
            ((RecyclerViewHolderItem)holder).userContainer.setTag(userModel);
            ((RecyclerViewHolderItem)holder).userContainer.setOnClickListener(clickListener);

            ((RecyclerViewHolderItem)holder).iconFavorite.setTag(userModel);
            ((RecyclerViewHolderItem)holder).iconFavorite.setOnClickListener(clickListener);

            if(!userModel.get_isFavorite())
                ((RecyclerViewHolderItem)holder).iconFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_border_green));
            else ((RecyclerViewHolderItem)holder).iconFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_star_green));
        } else {
            ((RecyclerViewHolderHeader)holder).headerText.setText(userModel.get_personName());
        }
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void addItems(List<UserModel> inUserModelList) {

        char firstChar;
        char currentChar = ' ';

        this.userModelList = new ArrayList<UserModel>();

        for (UserModel model : inUserModelList){
            firstChar = model.get_personName().toCharArray()[0];
            if( firstChar != currentChar ){
                currentChar = firstChar;
                UserModel userModel = new UserModel();
                userModel.set_isFirst(true);
                userModel.set_personName(String.valueOf(firstChar));
                this.userModelList.add(userModel);
                model.set_isFirst(false);
                this.userModelList.add(model);
            } else {
                model.set_isFirst(false);
                this.userModelList.add(model);
            }
        }

        notifyDataSetChanged();
    }

    public static class RecyclerViewHolderItem extends RecyclerView.ViewHolder{
        private View userContainer;
        private TextView nameTextView;
        private CircleImageView circleImageProfilePicture;
        private ImageView iconFavorite;

        public RecyclerViewHolderItem(View view) {
            super(view);
            userContainer = (View)view.findViewById(R.id.v_user_container);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            circleImageProfilePicture = (CircleImageView)view.findViewById(R.id.circleImageProfilePicture);
            iconFavorite = (ImageView)view.findViewById(R.id.icon_favorite);
        }
    }

    public static class RecyclerViewHolderHeader extends RecyclerView.ViewHolder{

        private TextView headerText;

        public RecyclerViewHolderHeader(View view) {
            super(view);
            headerText = (TextView)view.findViewById(R.id.tv_header);
        }
    }
}
