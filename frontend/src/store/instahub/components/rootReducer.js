import {combineReducers} from "redux";
import authenticationReducer from "./authentication/reducers/authenticationReducer";
import userReducer from "./users/reducers/userReducer";
import profileReducer from "./profile/reducers/profileReducer";
import followsReducer from "./subscription/reducers/followsReducer";
import followersReducer from "./subscription/reducers/followersReducer";

export const rootReducer = combineReducers({
    authenticationReducer,
    userReducer,
    profileReducer,
    followsReducer,
    followersReducer
});
