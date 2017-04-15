package com.example.absurd.dpclient.containers;

public class DocContainer {
    private String codeDoc;
    private String avtorDoc;
    private String messageDoc;

    public DocContainer(String codeDoc, String avtorDoc, String messageDoc) {
        this.codeDoc = codeDoc;
        this.avtorDoc = avtorDoc;
        this.messageDoc = messageDoc;
    }

    public String getCodeDoc() {
        return codeDoc;
    }

    public void setCodeDoc(String codeDoc) {
        this.codeDoc = codeDoc;
    }

    public String getAvtorDoc() {
        return avtorDoc;
    }

    public void setAvtorDoc(String avtorDoc) {
        this.avtorDoc = avtorDoc;
    }

    public String getMessageDoc() {
        return messageDoc;
    }

    public void setMessageDoc(String messageDoc) {
        this.messageDoc = messageDoc;
    }
}
