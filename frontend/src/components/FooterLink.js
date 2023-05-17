import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


export default function FooterLink({href, icon, bgColor}) {
    return (
        <a 
            href={href} 
            target="_blank" 
            rel="noreferrer"
            className="btn btn-primary border-0 shadow p-2 mx-2" 
            style={{backgroundColor: bgColor}}
        >
            <FontAwesomeIcon icon={ icon } className="fa-xl fa-fw" />
        </a>
    )
}