package ru.job4j.dream.servlet;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.MemStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {
    @Test
    public void whenCreateUser() throws ServletException, IOException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);

        User testUser = new User(0, "Name", "Email", "Password");
        store.saveUser(testUser);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn(String.valueOf(testUser.getId()));
        PowerMockito.when(req.getParameter("name")).thenReturn(testUser.getName());
        PowerMockito.when(req.getParameter("email")).thenReturn(testUser.getEmail());
        PowerMockito.when(req.getParameter("password")).thenReturn(testUser.getPassword());

        new RegServlet().doPost(req, resp);

        User result = store.findAllUsers().iterator().next();
        Assert.assertThat(result.getId(), Is.is(1));
        Assert.assertThat(result.getName(), Is.is("Name"));
        Assert.assertThat(result.getEmail(), Is.is("Email"));
        Assert.assertThat(result.getPassword(), Is.is("Password"));
    }
}