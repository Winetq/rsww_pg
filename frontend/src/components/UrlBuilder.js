
class UrlBuilder {
    build(...varNames) {
        let url = '';
        for(let varName of varNames){
            url += process.env[varName]; 
        }
        return url;
    }
}

export default UrlBuilder;
