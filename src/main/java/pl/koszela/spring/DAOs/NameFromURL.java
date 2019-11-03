package pl.koszela.spring.DAOs;

class NameFromURL {

    String getName(String url) {
        int indexByChar = url.lastIndexOf("/");
        int indexBeforeChar = url.lastIndexOf(".");
        return url.substring(indexByChar + 1, indexBeforeChar);
    }
}
