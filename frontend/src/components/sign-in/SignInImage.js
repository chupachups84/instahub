import nature_image from "../../icons/nature.jpg";
import React, {Component} from "react";
import '../sign-in/SignInImage.css';

class SignInImage extends Component {

    render() {
        return (
            <div className={"sign-in__image"}>
                <img className={"image-source"} src={nature_image} alt={"welcome picture"}/>
            </div>
        );
    }
}

export default SignInImage;
