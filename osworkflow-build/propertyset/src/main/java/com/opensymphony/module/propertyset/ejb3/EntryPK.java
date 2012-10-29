package com.opensymphony.module.propertyset.ejb3;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Hani Suleiman
 *         Date: Nov 8, 2005
 *         Time: 5:57:51 PM
 */
@Embeddable
public class EntryPK implements Serializable
{
  private static final long serialVersionUID = -6909978213448050937L;
  private String entityName;
  private long entityId;
  
  @Column(name="keyName")
  private String key;

  public EntryPK()
  {
  }

  public EntryPK(String entityName, long entityId, String key)
  {
    this.entityName = entityName;
    this.entityId = entityId;
    this.key = key;
  }

  public String getEntityName()
  {
    return entityName;
  }

  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  public long getEntityId()
  {
    return entityId;
  }

  public void setEntityId(long entityId)
  {
    this.entityId = entityId;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public boolean equals(Object o)
  {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    final EntryPK entryPK = (EntryPK)o;

    if(entityId != entryPK.entityId) return false;
    if(!entityName.equals(entryPK.entityName)) return false;
    if(!key.equals(entryPK.key)) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = entityName.hashCode();
    result = 29 * result + (int)(entityId ^ (entityId >>> 32));
    result = 29 * result + key.hashCode();
    return result;
  }
}
