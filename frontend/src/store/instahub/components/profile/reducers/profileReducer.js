import {
    FETCH_PHOTOS_FAILED,
    FETCH_PHOTOS_SUCCEEDED,
} from "../actions/profileActionTypes";

const photos = JSON.parse(localStorage.getItem("profilePhotos"));

const initialState = photos
    ? {
        isFetching: true,
        photos
    }
    : {
        isFetching: false,
        photos: []
    };

export default function (state = initialState, action) {

    const { type, payload } = action;

    switch (type) {
        case FETCH_PHOTOS_SUCCEEDED:
            return {
                ...state,
                isFetching: true,
                photos: payload.photos,
            };
        case FETCH_PHOTOS_FAILED:
            return {
                ...state,
                isFetching: false,
                photos: [],
            };
        default:
            return state;
    }
}