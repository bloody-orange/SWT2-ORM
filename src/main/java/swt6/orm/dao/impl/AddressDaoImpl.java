package swt6.orm.dao.impl;

import swt6.orm.dao.AddressDao;
import swt6.orm.domain.Address;
import swt6.orm.domain.AddressId;

public class AddressDaoImpl extends AbstractBaseDao<Address, AddressId> implements AddressDao {
    public AddressDaoImpl() {
        super(Address.class);
    }
}
