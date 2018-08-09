package mx.inigofr.specialfriends.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by inigo on 08/08/18.
 */

@Entity
public class UserModel {

    @NonNull
    @PrimaryKey
    public String _id;

    private String _personName;

    private String _imgURL;

    private String _imgID;

    private boolean _isFavorite;

    private boolean _isFirst;

    private String _phoneNumber;

    private String _Notes;

    private String _birthday;

    public UserModel() {
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_personName() {
        return _personName;
    }

    public void set_personName(String _personName) {
        this._personName = _personName;
    }

    public String get_birthday() {
        return _birthday;
    }

    public void set_birthday(String _birthday) {
        this._birthday = _birthday;
    }

    public String get_imgURL() {
        return _imgURL;
    }

    public void set_imgURL(String _imgURL) {
        this._imgURL = _imgURL;
    }

    public String get_imgID() {
        return _imgID;
    }

    public void set_imgID(String _imgID) {
        this._imgID = _imgID;
    }

    public boolean get_isFavorite() {
        return _isFavorite;
    }

    public void set_isFavorite(boolean _isFavorite) {
        this._isFavorite = _isFavorite;
    }

    public boolean is_isFirst() {
        return _isFirst;
    }

    public void set_isFirst(boolean _isFirst) {
        this._isFirst = _isFirst;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public String get_Notes() {
        return _Notes;
    }

    public void set_Notes(String _Notes) {
        this._Notes = _Notes;
    }
}
