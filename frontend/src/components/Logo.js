import {Component} from 'react';
import '../components/Logo.css'

class Logo extends Component {
    render() {
        return(
            <div className={"logo"}>
                <button className={"logo__button"}></button>
                <div className={"logo__project-name"}>Instahub</div>
            </div>
        );
    }
}

export default Logo;
