package poly.edu.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Entity.Accounts;
import poly.edu.Repository.AccountsRepository;

@Service
public class AccountsServiceImpl implements AccountsService {
	@Autowired
	AccountsRepository accountsRepo;

//	@Override
//	public List<Accounts> findAll() {
//		return accountsRepo.findAll();
//	}

	@Override
	public Accounts findById(String username) {
		Optional<Accounts> acc = accountsRepo.findById(username);
		if (acc.isPresent())
			return acc.get();
		return null;
	}

	@Override
	public Accounts create(Accounts entity) {
		return accountsRepo.save(entity);
	}

	@Override
	public void update(Accounts entity) {
		accountsRepo.save(entity);

	}

	@Override
	public void deleteById(String username) {
		accountsRepo.deleteById(username);

	}

	@Override
	public Boolean existsById(String username) {
		return accountsRepo.existsById(username);
	}

//	@Override
//	public List<Accounts> search(String keyword) {
//		return accountsRepo.findByUsernameContainingOrFullnameContaining(keyword, keyword);
//	}

	@Override
	public Page<Accounts> findAll(Pageable pageable) {
		return accountsRepo.findAll(pageable);
	}

	@Override
	public Page<Accounts> search(String keyword, Pageable pageable) {
		return accountsRepo.findByUsernameContainingOrFullnameContaining(keyword, keyword, pageable);
	}

}
