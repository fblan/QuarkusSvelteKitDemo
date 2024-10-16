<script lang="ts">
    import {invalidateAll} from "$app/navigation";
    import toast, {Toaster} from "svelte-french-toast";
    export let callback=()=>{}

    export let todo;

    async function editTodo(event: Event){
        const formEl = event.target as HTMLFormElement;
        const data = new FormData(formEl);
        console.log("formel", formEl);
        console.log("data", data);
        const response = await fetch("rest/todo/"+data.get("id"),{
            method:'POST',
            body:data
        });

        const responseData = await response.json();
        toast.success("edited success!!!!");
        formEl.reset();
        console.log("edit response : ",responseData);
        callback();

    }

</script>

<form on:submit|preventDefault={editTodo}>
    <input type="hidden" name="id" value={todo.id}/>
    <input type="text" name="todo" value={todo.text}/>
    <button type="submit">Save</button>
    <button type="secondary" on:click={callback}>cancel</button>
</form>



