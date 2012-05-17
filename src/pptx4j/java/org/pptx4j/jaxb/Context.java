/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */
package org.pptx4j.jaxb;


import ae.com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import ae.com.sun.xml.bind.v2.model.annotation.XmlSchemaMine;
import ae.javax.xml.bind.JAXBContext;
import ae.javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.docx4j.dml.Graphic;
import org.docx4j.dml.chart.CTChart;
import org.docx4j.relationships.Relationships;

public class Context {
	
	/*
	 * Two reasons for having a separate class for this:
	 * 1. so that loading PML context does not slow
	 *    down docx4j operation on docx files
	 * 2. to try to maintain clean delineation between
	 *    docx4j and pptx4j
	 */
	
	public static JAXBContext jcPML;
	
	private static Logger log = Logger.getLogger(Context.class);
	
	static {

		// Display diagnostic info about version of JAXB being used.
    	Class c;
    	try {
    		c = Class.forName("com.sun.xml.bind.marshaller.MinimumEscapeHandler");
    		System.out.println("JAXB: Using RI");
    	} catch (ClassNotFoundException cnfe) {
    		// JAXB Reference Implementation not present
    		System.out.println("JAXB: RI not present.  Trying Java 6 implementation.");
        	try {
				c = Class.forName("com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler");
	    		System.out.println("JAXB: Using Java 6 implementation.");
			} catch (ClassNotFoundException e) {
				System.out.println("JAXB: neither Reference Implementation nor Java 6 implementation present?");
			}
    	}
		
		// Android doesn't support package annotations, so 
		// tell our JAXB about our namespaces
      
		RuntimeInlineAnnotationReader.cachePackageAnnotation(Relationships.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/presentationml/2006/main"));
    			
		//DrawingML
		RuntimeInlineAnnotationReader.cachePackageAnnotation(Graphic.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/main")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(CTChart.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/chart")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.chartDrawing.CTDrawing.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.compatibility.CTCompat.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/compatibility")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.diagram.CTDiagramDefinition.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/diagram")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.diagram2008.CTDrawing.class.getPackage(), 
				new XmlSchemaMine("http://schemas.microsoft.com/office/drawing/2008/diagram")); 
		
		// LockedCanvas?
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.picture.Pic.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/picture")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.spreadsheetdrawing.CTDrawing.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing")); 
		
		RuntimeInlineAnnotationReader.cachePackageAnnotation(org.docx4j.dml.wordprocessingDrawing.Inline.class.getPackage(), 
				new XmlSchemaMine("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing")); 
		
		try {	
			
			// JBOSS might use a different class loader to load JAXBContext, which causes problems,
			// so explicitly specify our class loader.
			Context tmp = new Context();
			java.lang.ClassLoader classLoader = tmp.getClass().getClassLoader();
			//log.info("\n\nClassloader: " + classLoader.toString() );			
			
			log.info("loading Context jcPML");			
			jcPML = JAXBContext.newInstance("org.pptx4j.pml:" +
					"org.docx4j.dml:org.docx4j.dml.chart:org.docx4j.dml.chartDrawing:org.docx4j.dml.compatibility:org.docx4j.dml.diagram:org.docx4j.dml.lockedCanvas:org.docx4j.dml.picture:org.docx4j.dml.wordprocessingDrawing:org.docx4j.dml.spreadsheetdrawing", 
					classLoader );
			log.info(".. loaded ..");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}				
	}
	
	
	public static org.pptx4j.pml.ObjectFactory pmlObjectFactory;
	public static org.pptx4j.pml.ObjectFactory getpmlObjectFactory() {
		
		if (pmlObjectFactory==null) {
			pmlObjectFactory = new org.pptx4j.pml.ObjectFactory();
		}
		return pmlObjectFactory;
		
	}
}