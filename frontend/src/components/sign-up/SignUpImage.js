import nature_image from "../../icons/nature.jpg";
import React, {Component} from "react";
import './SignUpImage.css';

class SignUpImage extends Component {

    render() {
        return (
            <div className={"sign-up__image"}>
                <img className={"image-source"} src={nature_image} alt={"welcome picture"}/>
            </div>
        );
    }
}

export default SignUpImage;
