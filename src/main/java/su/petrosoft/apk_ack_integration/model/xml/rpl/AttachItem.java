package su.petrosoft.apk_ack_integration.model.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record AttachItem(
        @JacksonXmlProperty(isAttribute = true, localName = "ATTACH_NAME") String attachName,
        @JacksonXmlProperty(isAttribute = true, localName = "ATTACHTYPE_TEXT") String attachtypeText,
        @JacksonXmlProperty(isAttribute = true, localName = "ATTACH_TYPE") String attachType,
        @JacksonXmlProperty(isAttribute = true, localName = "DESCRIPTION") String description,
        @JacksonXmlProperty(isAttribute = true, localName = "DOCUMENT_ID") String documentId,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_TYPE_CODE") String docTypeCode,
        @JacksonXmlProperty(isAttribute = true, localName = "FILE_EXT_ID") String fileExtId,
        @JacksonXmlProperty(isAttribute = true, localName = "FILE_UUID") String fileUuid,
        @JacksonXmlProperty(isAttribute = true, localName = "LINK_DEVICE_NAME") String linkDeviceName,
        @JacksonXmlProperty(isAttribute = true, localName = "OBJECT_EXT_ID") String objectExtId,
        @JacksonXmlProperty(isAttribute = true, localName = "OBJECT_TYPE") String objectType,
        @JacksonXmlProperty(isAttribute = true, localName = "STORAGE_NAME") String storageName,
        @JacksonXmlProperty(isAttribute = true, localName = "SYSTEM_CODE") String systemCode,

        @JacksonXmlProperty(localName = "BODY") Body body
) {}
