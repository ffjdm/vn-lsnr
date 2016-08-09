package com.cvs.versionlistener;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

public class IncreasedVersionListener implements EventListener {

	private static final Log log = LogFactory.getLog(IncreasedVersionListener.class);

	public static final String DOCUMENT_MAJOR_VERSION_INCREMENT = "documentMajorVersionIncrement";

    @Override
    public void handleEvent(Event event) {
    	log.trace("BEGIN");

        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
          return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();

        if (doc != null) {
        	String versionLabel = doc.getVersionLabel();
        	log.trace("doc = " + doc);
        	log.trace("version label = " + versionLabel);

        	if (versionLabel.endsWith(".0")) {
        		log.debug("major version increment -> " + versionLabel);

        		Event newEvent = ctx.newEvent(DOCUMENT_MAJOR_VERSION_INCREMENT);
        		event.setIsCommitEvent(true);
        		event.setInline(true);
        		Framework.getLocalService(EventService.class).fireEvent(newEvent);

        		log.debug("event " + DOCUMENT_MAJOR_VERSION_INCREMENT + " fired.");
        	} else {
        		log.debug("minor version increment (nothing to do) -> " + versionLabel);
        	}
        } else {
        	log.error("doc is null!");
        }

        // Add some logic starting from here.
        log.trace("END");
    }
}
