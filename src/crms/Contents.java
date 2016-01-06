/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Orlon
 */
@Entity
@Table(name = "contents", catalog = "crms", schema = "")
@NamedQueries({
    @NamedQuery(name = "Contents.findAll", query = "SELECT c FROM Contents c"),
    @NamedQuery(name = "Contents.findBySections", query = "SELECT c FROM Contents c WHERE c.sections = :sections"),
    @NamedQuery(name = "Contents.findByParticulars", query = "SELECT c FROM Contents c WHERE c.particulars = :particulars")})
public class Contents implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Sections")
    private String sections;
    @Column(name = "Particulars")
    private String particulars;

    public Contents() {
    }

    public Contents(String sections) {
        this.sections = sections;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        String oldSections = this.sections;
        this.sections = sections;
        changeSupport.firePropertyChange("sections", oldSections, sections);
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        String oldParticulars = this.particulars;
        this.particulars = particulars;
        changeSupport.firePropertyChange("particulars", oldParticulars, particulars);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sections != null ? sections.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contents)) {
            return false;
        }
        Contents other = (Contents) object;
        if ((this.sections == null && other.sections != null) || (this.sections != null && !this.sections.equals(other.sections))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crms.Contents[ sections=" + sections + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
