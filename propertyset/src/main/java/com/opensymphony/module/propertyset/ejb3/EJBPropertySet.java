package com.opensymphony.module.propertyset.ejb3;

import javax.ejb.Remove;

import com.opensymphony.module.propertyset.PropertySet;

/**
 * @author Hani Suleiman
 *         Date: Jul 30, 2006
 *         Time: 11:41:29 AM
 */
public interface EJBPropertySet extends PropertySet
{
  void setEntityId(Long entityId);

  void setEntityName(String entityName);

  @Remove
  void destroy();

  /**
   * Clear the entity manager
   */
  void clear();
}
