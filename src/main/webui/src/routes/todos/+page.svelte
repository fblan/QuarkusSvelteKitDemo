<script lang="ts">
    import type { PageData } from './$types';
    import {invalidateAll} from "$app/navigation";
    import ToDoRead from "./ToDoRead.svelte";

    export let data: PageData;

    async function addTodo(event: Event){
        const formEl = event.target as HTMLFormElement;
        const data = new FormData(formEl);
        const value = Object.fromEntries(data.entries());
        const response = await fetch("rest/todo",{
            method:formEl.method,
            body:data
        });

        const responseData = await response.json();
        formEl.reset();
        await invalidateAll();
        console.log("response : ",responseData);

    }
    async function removeTodo(event: Event){
        const formEl = event.target as HTMLFormElement;
        const data = new FormData(formEl);

        const value = Object.fromEntries(data.entries());
        const response = await fetch("rest/todo/"+data.get("id"),{
            method:'DELETE',
        });

        const responseData = await response.json();
        formEl.reset();
        await invalidateAll();
        console.log("response : ",responseData);
    }


</script>

<ul>
    {#each data.todos as todo}
        <li>
            <span><ToDoRead todo={todo}/></span>
            <form on:submit|preventDefault={removeTodo} enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="id" value={todo.id}/>
                <button class="delete" type="submit">X</button>
            </form>
        </li>
    {/each}
</ul>

<form on:submit|preventDefault={addTodo} method="POST" action="rest/todo">
    <input type="text" name="todo" />
    <input type="text" name="author" />
    <button type="submit">+ Add ToDo</button>
</form>
